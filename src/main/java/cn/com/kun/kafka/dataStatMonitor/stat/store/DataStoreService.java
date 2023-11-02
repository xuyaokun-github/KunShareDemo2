package cn.com.kun.kafka.dataStatMonitor.stat.store;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public interface DataStoreService {


    void addToAccumulatedTopicSet(String topicName);


    void removeFromSet(String topicName);


    void removeExpiredData(String topicName);


    List<String> getAllAccumulatedTopicFromSet();

    void addProducerHostName(String hostname);

    void countProducerData(String hostname, String topic, Map<String, AtomicLong> producerCountMap);


    void sendHostnameHeartBeat(String hostname);


    void addConsumerHostName(String hostname);


    void countConsumerData(String hostname, String topic, Map<String, AtomicLong> consumerCountMap);


    String collectData();

}



