package cn.com.kun.kafka.dataStatMonitor.scheduled;

import cn.com.kun.common.utils.InetAddressUtils;
import cn.com.kun.kafka.dataStatMonitor.properties.KafkaDataStatMonitorProperties;
import cn.com.kun.kafka.dataStatMonitor.stat.counter.TopicDataStatCounter;
import cn.com.kun.kafka.dataStatMonitor.stat.store.DataStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 生产者数据上报调度
 *
 * author:xuyaokun_kzx
 * date:2023/11/1
 * desc:
*/
@EnableScheduling
@Component
public class ProducerDataReportSchedule {

    @Autowired
    private KafkaDataStatMonitorProperties kafkaDataStatMonitorProperties;

    @Autowired
    private DataStoreService dataStoreService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void scheduled(){

        if (!kafkaDataStatMonitorProperties.isProducerReportEnabled()){
            return;
        }
        //上报主机名
        String hostname = InetAddressUtils.getHostName();
        //发送主机名心跳
        dataStoreService.sendHostnameHeartBeat(hostname);

        List<String> accumulatedTopics = dataStoreService.getAllAccumulatedTopicFromSet();
        if (!CollectionUtils.isEmpty(accumulatedTopics)){
            for (String topic : accumulatedTopics) {
                //上报主机名
                dataStoreService.addProducerHostName(hostname);
                //上报生产者采集到的数据
                dataStoreService.countProducerData(hostname, topic, TopicDataStatCounter.getProducerCountMap(topic));
            }
        }
        TopicDataStatCounter.clearExpiredData(accumulatedTopics);
    }

}
