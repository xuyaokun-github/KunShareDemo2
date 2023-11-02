package cn.com.kun.kafka.dataStatMonitor.scheduled;

import cn.com.kun.kafka.dataStatMonitor.stat.notice.MonitorNoticeService;
import cn.com.kun.kafka.dataStatMonitor.stat.store.DataStoreService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * cn.com.kun.common.utils.InetAddressUtils#getHostName()
 *
 * author:xuyaokun_kzx
 * date:2023/10/25
 * desc:
*/
public class DataCollectReportScheduleTask {

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private final static Logger LOGGER = LoggerFactory.getLogger(DataCollectReportScheduleTask.class);


    private static DataStoreService dataStoreService;

    private static MonitorNoticeService monitorNoticeService;

    /**
     * 调度间隔
     */
    private static long scheduledIntervalMs;

    /**
     * 无论是生产端还是消费端，每个节点都是同时上报，选择的调度方式是cron方式调度
     * 本类作为一个独立的数据整合端，每隔30秒采集一次已经够用了
     */
    public static void initSchedule(DataStoreService storeService, MonitorNoticeService noticeService) {

        dataStoreService = storeService;
        monitorNoticeService = noticeService;

        scheduledExecutorService.scheduleAtFixedRate(()->{

//            LOGGER.info("DataCollectReportScheduleTask Running");
            try {
                String result = dataStoreService.collectData();
                if (StringUtils.isNotEmpty(result)){
                    //发出监控通知
                    monitorNoticeService.notice(result);
                }
            }catch (Exception e){
                LOGGER.error("DataCollectReportScheduleTask异常", e);
            }

        }, 60L, scheduledIntervalMs/1000, TimeUnit.SECONDS);

    }

    /**
     * 上层扩展点：自定义监控通知实现
     *
     * @param noticeService
     */
    public static void registerMonitorNoticeService(MonitorNoticeService noticeService){
        monitorNoticeService = noticeService;
    }

    public static void initDataCollectScheduledIntervalMs(long dataCollectScheduledIntervalMs) {
        scheduledIntervalMs = dataCollectScheduledIntervalMs;
    }
}
