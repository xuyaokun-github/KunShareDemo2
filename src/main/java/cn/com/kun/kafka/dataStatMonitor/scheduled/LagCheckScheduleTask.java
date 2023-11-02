package cn.com.kun.kafka.dataStatMonitor.scheduled;

import cn.com.kun.kafka.dataStatMonitor.lag.TopicLagMonitor;
import cn.com.kun.kafka.dataStatMonitor.stat.store.DataStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Lag检测调度器
 * 通常放在定时任务工程
 *
 * author:xuyaokun_kzx
 * date:2023/8/31
 * desc:
*/
public class LagCheckScheduleTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(LagCheckScheduleTask.class);

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private static TopicLagMonitor topicLagMonitor;

    private static DataStoreService dataStoreService;

    private static Map<String, Long> lagTimeCache = new HashMap<>();

    /**
     * 默认正常时间间隔：1分钟
     * 超过一分钟则告警
     */
    private static long NORMAL_TIME_INTERVAL = 60 * 1000;

    public static void initSchedule(TopicLagMonitor lagMonitor, DataStoreService storeService) {

        //初始化服务层实现
        topicLagMonitor = lagMonitor;
        dataStoreService = storeService;

        scheduledExecutorService.scheduleAtFixedRate(()->{

            LOGGER.info("LagCheckScheduleTask Running");
            try {
                //检测是否存在堆积中的主题
                Map<String, Long> topicsLagInfo =  topicLagMonitor.getAllTopicsLagInfo();
                topicsLagInfo.forEach((topicName, lag) ->{

                    if (lag > 0){
                        //假如一个主题的lag,已经连续N分钟出现堆积，则达到告警的条件
                        if (!lagTimeCache.containsKey(topicName)){
                            lagTimeCache.put(topicName, System.currentTimeMillis());
                        }else if (reachNoticeCondition(topicName)){
                            //发送“通知”
                            sendAccumulatedTopicNotice(topicName);
                        }
                    }else {
                        //该主题不再堆积
                        //移除记录的时间戳
                        lagTimeCache.remove(topicName);
                        //remove存储的旧数据
                        removeExpiredData(topicName);
                    }
                });


            }catch (Exception e){
                LOGGER.error("LagCheckScheduleTask异常", e);
            }
        }, 0L, 10L, TimeUnit.SECONDS);
    }

    private static void removeExpiredData(String topicName) {

        //
        dataStoreService.removeFromSet(topicName);
        dataStoreService.removeExpiredData(topicName);
    }

    /**
     * 判断是否满足通知条件
     *
     * @param topicName
     * @return
     */
    private static boolean reachNoticeCondition(String topicName) {

        return lagTimeCache.containsKey(topicName) && System.currentTimeMillis() - lagTimeCache.get(topicName) > NORMAL_TIME_INTERVAL;
    }

    private static void sendAccumulatedTopicNotice(String topicName) {

        LOGGER.info("主题{}出现堆积", topicName);
        dataStoreService.addToAccumulatedTopicSet(topicName);
    }

    /**
     * 初始化监控间隔
     *
     * @param normalTimeIntervalMs
     */
    public static void initNormalTimeIntervalMs(long normalTimeIntervalMs) {
        NORMAL_TIME_INTERVAL = normalTimeIntervalMs;
    }
}
