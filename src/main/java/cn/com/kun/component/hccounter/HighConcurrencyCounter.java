package cn.com.kun.component.hccounter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 高并发增量计数器V1.1
 *
 * author:xuyaokun_kzx
 * date:2024/5/19
 * desc:
*/
public class HighConcurrencyCounter {

    private final static Logger LOGGER = LoggerFactory.getLogger(HighConcurrencyCounter.class);

    /**
     * 业务类型
     * 多个不同的业务场景可以分别定义各自的计数器
     */
    private String bizType;

    /**
     * 最大版本集合
     * key: 统计key
     * value: 最大版本记录器
     */
    private Map<String, AtomicLong> countKeyVersionMap = new ConcurrentHashMap<>();

    /**
     * 时间戳集合
     * key: 统计key
     * value: 最近一次发现计数为0的时间戳
     */
    private Map<String, Long> countKeyTimestampMap = new ConcurrentHashMap<>();

    /**
     * 计数器集合（同一个统计Key可能有多个计数器）
     * key: 统计key+版本号
     * value: 计数器
     */
    private Map<String, AtomicLong> counterMap = new ConcurrentHashMap<>();

    /**
     * 待移除的统计key
     *
     */
    private Set<String> needRemoveCountKeys = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * 数值更新实现（基于接口编程）
     */
    private CountUpdateFunction countUpdateFunction;

    /**
     * 更新间隔(使用方可以定制，可快可慢)
     */
    private long updateIntervalMs = 10000;

    private boolean threadWorkEnabled = true;

    private long heartBeatTime = System.currentTimeMillis();

    private final static String SPLIT_STR = "_";

    public HighConcurrencyCounter(String bizType) {
        this.bizType = bizType;
    }

    public void start(){

        String threadName = "HighConcurrencyCounter-" + bizType;
        //启动线程
        new Thread(()->{
            while (threadWorkEnabled){
                doWork();
            }
        }, threadName).start();
    }

    /**
     * 组件线程工作
     */
    private void doWork() {

        //移除所有暂停统计的key
        removeStoppedCountKey();

        //遍历当前在统计的所有统计key
        countVersionIncrement();

        //处理所有历史统计器
        processHistoryCounter();

        //每隔一段时间输出一下当前的countKey
        showCountKey();

        //清理一天之内，最大版本号都没变化的计数器（1天一次）
        clearExpiredCountKey();

        //睡眠，等待下一次处理
        sleep(updateIntervalMs);

    }


    /**
     * 假如一直数值没变，默认超出1天，则主动删除
     *
     */
    private void clearExpiredCountKey() {

        //放到stop集合里即可
        if (countKeyTimestampMap.size() > 0){
            countKeyTimestampMap.forEach((countKey, timestamp)->{
                if (System.currentTimeMillis() - timestamp > 24 * 3600 * 1000){
                    //数值为0的时间超出24小时，自动清理
                    remove(countKey);
                }
            });
        }

    }

    private void countVersionIncrement() {

        Set<String> countKeySet = countKeyVersionMap.keySet();
        countKeySet.forEach(countKey ->{
            AtomicLong atomicLong = countKeyVersionMap.get(countKey);
            if (atomicLong != null){
                //创建下一个版本的计数器（确保主线程一定能拿到计数器，主线程无需判断是否需要创建计数器，省下了主线程之间的锁竞争）
                long currentMaxVersion = atomicLong.get();
                String key = buildVersionKey(countKey, currentMaxVersion+1);
                counterMap.put(key, new AtomicLong(0));
                if (counterMap.get(key) != null){//避免编译器重排序
                    atomicLong.incrementAndGet();//最大版本加1
                }
            }
        });

        //适当睡眠500(也可以不加)
//        sleep(500);
    }

    private void removeStoppedCountKey() {

        if (needRemoveCountKeys.size() > 0){
            List<String> removeCountKeyList = new ArrayList<>();
            needRemoveCountKeys.forEach(countKey ->{
                removeCountKeyList.add(countKey);
                countKeyVersionMap.remove(countKey);
                //移除该统计key的所有统计器
                Iterator<Map.Entry<String, AtomicLong>> iterator = counterMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, AtomicLong> entry = iterator.next();
                    String key = entry.getKey();
                    //获取对应的统计Key
                    key = getCountKey(key);
                    if (key.contains(countKey)) {
                        iterator.remove();
                    }
                }
            });
            removeCountKeyList.forEach(countKey -> {
                needRemoveCountKeys.remove(countKey);
            });
        }

    }

    private String getCountKey(String key) {

        int index = key.lastIndexOf(SPLIT_STR);
        return key.substring(0, index);
    }

    private void processHistoryCounter() {

        //获取当前统计key的所有历史版本计数器
        countKeyVersionMap.forEach((countKey, version)->{

            long maxVersion = version.get();//当前最大版本号
            while (true){
                String key = buildVersionKey(countKey, (maxVersion - 1));
                AtomicLong counter = counterMap.get(key);
                if (counter != null){
                    //计数器执行setAndGet
                    long currentValue = counter.getAndSet(Long.MIN_VALUE);
                    //执行更新
                    countUpdate(countKey, currentValue);
                    //该值已经被更新，该计数器不会再被用到了，可以移除
                    counterMap.remove(key);
                    //记录计数器变化的时间戳
                    recordCountChangeTimestamp(countKey, currentValue);

                }else {
                    //全部历史版本处理完毕
                    break;
                }
            }
        });
    }

    private String buildVersionKey(String countKey, long version) {

        return countKey + SPLIT_STR + version;
    }

    /**
     * 假如使用方停止计数且不主动移除统计Key,版本号仍在持续增加，每次的统计值必然是0
     *
     * @param countKey
     * @param currentValue
     */
    private void recordCountChangeTimestamp(String countKey, long currentValue) {

        if (currentValue > 0){
            //说明仍在计数，移除时间戳
            countKeyTimestampMap.remove(countKey);
        }else {
            //数据为0，说明不再计数了，可以记录时间戳
            //假如时间戳已经有了，就不再重复记录
            if (!countKeyTimestampMap.containsKey(countKey)){
                countKeyTimestampMap.put(countKey, System.currentTimeMillis());
            }
        }

    }

    private void showCountKey() {

        if (System.currentTimeMillis() - heartBeatTime > 3600 * 1000){
            StringBuilder builder = new StringBuilder();
            builder.append("当前计数器情况：");
            countKeyVersionMap.forEach((countKey, version)->{
                builder.append(String.format("统计key:%s 当前版本号：%s;", countKey, version.get()));
            });
            LOGGER.info(builder.toString());
            heartBeatTime = System.currentTimeMillis();
        }

    }

    private void countUpdate(String countKey, long currentValue) {

        try {
            countUpdateFunction.update(countKey, currentValue);
        }catch (Exception e){
            //出现此类问题，需要上游定位排查
            LOGGER.warn("计数值更新实现出现异常", e);
        }

    }

    private void sleep(long sleepTime) {

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            LOGGER.error("高并发增量计数器中断异常", e);
        }
    }

    /**
     * 使用方主线程调用
     * 1.根据统计key找到当前最大的版本号
     * 2.根据最大版本找到对应的计数器对象
     * 3.计数器进行加1（也可以设计为加N，由使用方自行决定）
     * 4.判断是否加一成功，假如加1返回值大于0，说明是成功的
     *
     * @param countKey
     * @param count
     */
    public void add(String countKey, long count){

        try {
            AtomicLong atomicLong = getMaxVerionCounter(countKey);
            if (atomicLong != null){
                long newValue = atomicLong.getAndAdd(count);
                if (newValue < 0){
                    //假如累加之后仍是负数，说明出现GC影响，导致线程顺序延迟，则继续尝试获取最大版本号，继续累加
                    add(countKey, count);
                }
            }else {
                LOGGER.error("计数器组件异常，获取不到计数器");
            }
        }catch (Exception e){
            //为了不影响主流程
            LOGGER.error("计数器组件计数异常", e);
        }


    }

    /**
     * 获取当前最大版本号
     *
     * @return
     * @param countKey
     */
    private AtomicLong getMaxVerionCounter(String countKey) {

        AtomicLong atomicLong = countKeyVersionMap.get(countKey);
        if (atomicLong == null){
            synchronized (countKeyVersionMap){
                atomicLong = countKeyVersionMap.get(countKey);
                if (atomicLong == null){
                    atomicLong = new AtomicLong(0);
                    //初始化第一个计数器
                    String versionKey = buildVersionKey(countKey, atomicLong.get());
                    counterMap.put(versionKey, new AtomicLong());
                    if (counterMap.get(versionKey) != null){//避免重排序
                        countKeyVersionMap.put(countKey, atomicLong);
                    }
                }
            }
        }

        long maxVersion = atomicLong.get();
//        System.out.println("当前最大版本：" + maxVersion);
        //根据最大版本号获取当前的计数器
        String key = buildVersionKey(countKey, maxVersion);
        AtomicLong counter = counterMap.get(key);
        return counter;
    }

    public void remove(String countKey){

        //将统计key放到待移除的set集合，等待组件线程进行移除
        needRemoveCountKeys.add(countKey);

    }

    /**
     * 只能关闭一次，该组件将彻底停止工作
     */
    public void stop(){
        threadWorkEnabled = false;
        countKeyVersionMap.clear();
        counterMap.clear();
    }

    public long getUpdateIntervalMs() {
        return updateIntervalMs;
    }

    public void setUpdateIntervalMs(long updateIntervalMs) {
        this.updateIntervalMs = updateIntervalMs;
    }

    public CountUpdateFunction getCountUpdateFunction() {
        return countUpdateFunction;
    }

    public void setCountUpdateFunction(CountUpdateFunction countUpdateFunction) {
        this.countUpdateFunction = countUpdateFunction;
    }


}
