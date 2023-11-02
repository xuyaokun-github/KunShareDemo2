package cn.com.kun.kafka.dataStatMonitor.stat.counter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/8/18
 * desc:
 * 注意一个问题，某个Pod可能没生产，但是可能会消费，所以执行减1时可能遇到负数
*/
public class TopicDataStatCounter {

    /**
     * 可能会有多个主题
     */
    private static final Map<String, Map<String, AtomicLong>> msgTypeProducerCountMap = new ConcurrentHashMap<>();

    private static final Map<String, Map<String, AtomicLong>> msgTypeConsumerCountMap = new ConcurrentHashMap<>();

    private static final Map<String, Long> checkTimeMap = new ConcurrentHashMap<>();

    /**
     * 计数加1
     * 考虑线程安全优化
     *
     * @param topic
     * @param msgType
     */
    public static void add(String topic, String msgType) {

        Map<String, AtomicLong> countMap = msgTypeProducerCountMap.get(topic);
        if (countMap == null){
            synchronized (msgTypeProducerCountMap){
                countMap = msgTypeProducerCountMap.get(topic);
                if (countMap == null){
                    countMap = new ConcurrentHashMap<>();
                    msgTypeProducerCountMap.put(topic, countMap);
                }
                if (countMap.containsKey(msgType)){
                    countMap.get(msgType).incrementAndGet();
                }else {
                    countMap.put(msgType, new AtomicLong(1));
                }
            }
        }else {
            AtomicLong count = countMap.get(msgType);
            if (count == null){
                synchronized (countMap){
                    count = countMap.get(msgType);
                    if (count == null){
                        countMap.put(msgType, new AtomicLong(1));
                    }else {
                        count.incrementAndGet();
                    }
                }
            }else {
                count.incrementAndGet();
            }
        }

    }

    /**
     * 计数减一(方法名虽然起名叫减一，但是实质还是累加，最终在汇总数会作为减数)
     *
     * @param topic
     * @param msgType
     */
    public static void subtract(String topic, String msgType) {

        Map<String, AtomicLong> countMap = msgTypeConsumerCountMap.get(topic);
        if (countMap == null){
            synchronized (msgTypeConsumerCountMap){
                countMap = msgTypeConsumerCountMap.get(topic);
                if (countMap == null){
                    countMap = new ConcurrentHashMap<>();
                    msgTypeConsumerCountMap.put(topic, countMap);
                }
                if (countMap.containsKey(msgType)){
                    countMap.get(msgType).incrementAndGet();
                }else {
                    countMap.put(msgType, new AtomicLong(1));
                }
            }
        }else {
            AtomicLong count = countMap.get(msgType);
            if (count == null){
                synchronized (countMap){
                    count = countMap.get(msgType);
                    if (count == null){
                        countMap.put(msgType, new AtomicLong(1));
                    }else {
                        count.incrementAndGet();
                    }
                }
            }else {
                count.incrementAndGet();
            }
        }

    }

    /**
     * 当监控到主题无堆积时，可清空整个数据（以防漏加漏减）
     * @param topic
     */
    public static void reset(String topic){

        Map<String, AtomicLong> producerCountMap = msgTypeProducerCountMap.get(topic);
        if (producerCountMap != null){
            producerCountMap.clear();
        }
        Map<String, AtomicLong> comsumerCountMap = msgTypeConsumerCountMap.get(topic);
        if (comsumerCountMap != null){
            comsumerCountMap.clear();
        }

    }

    /**
     * 获取已统计总数(生产者)
     *
     * @param topic
     * @return
     */
    public static Map<String, AtomicLong> getProducerCountMap(String topic) {

        return msgTypeProducerCountMap.get(topic);
    }

    /**
     * 获取已统计总数(消费者)
     * @param topic
     * @return
     */
    public static Map<String, AtomicLong> getConsumerCountMap(String topic) {

        return msgTypeConsumerCountMap.get(topic);
    }

    /**
     * 必须清理旧数据，且不能清理得太频繁，否则会造成监控失真
     *
     * @param accumulatedTopics
     */
    public static void clearExpiredData(List<String> accumulatedTopics) {

        Set<String> accumulatedTopicSet = null;
        if (accumulatedTopics != null && accumulatedTopics.size() >0){
            accumulatedTopicSet = new HashSet<>(accumulatedTopics);
        }

        if (!msgTypeProducerCountMap.isEmpty()){
            clearExpiredData(accumulatedTopicSet, msgTypeProducerCountMap);
        }

        if (!msgTypeProducerCountMap.isEmpty()){
            clearExpiredData(accumulatedTopicSet, msgTypeProducerCountMap);
        }

    }

    private static void clearExpiredData(Set<String> accumulatedTopicSet, Map<String, Map<String, AtomicLong>> countMap) {

        countMap.forEach((topic, msgTypeCountMap)->{

            if (!isAccumulatedTopic(topic, accumulatedTopicSet) && reachClearTime(topic)){
                //清理的动作不调用clear，而是将计数器置零
                Map<String, AtomicLong> msgTypeValueMap = countMap.get(topic);
                msgTypeCountMap.keySet().forEach(key -> {
                    msgTypeValueMap.put(key, new AtomicLong(0));
                });
                //记录清理时间点
                checkTimeMap.put(topic, System.currentTimeMillis());
            }

        });

    }

    /**
     * 是否为已堆积主题
     *
     * @param topic
     * @param accumulatedTopicSet
     * @return
     */
    private static boolean isAccumulatedTopic(String topic, Set<String> accumulatedTopicSet) {

        return accumulatedTopicSet != null && accumulatedTopicSet.contains(topic);
    }

    /**
     * 判断是否到达清理时间
     *
     * @param topic
     * @return
     */
    private static boolean reachClearTime(String topic) {

        if (!checkTimeMap.containsKey(topic)){
            //未包含该主题的时间戳，则说明未检测过，无需清理
            return false;
        }

        return checkTimeMap.containsKey(topic) && System.currentTimeMillis() - checkTimeMap.get(topic) > 10 * 60 * 1000;
    }
}
