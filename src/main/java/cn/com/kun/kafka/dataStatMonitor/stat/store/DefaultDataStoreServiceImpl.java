package cn.com.kun.kafka.dataStatMonitor.stat.store;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static cn.com.kun.kafka.dataStatMonitor.constants.DataStoreConstant.*;

/**
 * 使用Redis做数据汇总
 *
 * author:xuyaokun_kzx
 * date:2023/10/26
 * desc:
*/
public class DefaultDataStoreServiceImpl implements DataStoreService{

    private RedisTemplate redisTemplate;

    public DefaultDataStoreServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void addToAccumulatedTopicSet(String topicName) {

        redisTemplate.opsForSet().add(ACCUMULATED_TOPIC, topicName);
    }

    @Override
    public void removeFromSet(String topicName) {

        redisTemplate.opsForSet().remove(ACCUMULATED_TOPIC, topicName);
    }

    /**
     * 删除所有过期数据（之前堆积的数据，现在不堆积了，这种就算过期数据）
     *
     * @param topicName
     */
    @Override
    public void removeExpiredData(String topicName) {

        //根据key进行删除
        //具体的删除方式是设置过期时间即可，让它自动清理
        //找到所有生产者hostname
        Set<String> producerHostnames = getAllProducerHostnames();
        for (String hostname : producerHostnames){
            //找到所有消息类型
            Set<String> msgTypes = redisTemplate.opsForSet().members(buildProducerHostnameTopicKey(hostname, topicName));
            if (!CollectionUtils.isEmpty(msgTypes)){
                redisTemplate.expire(buildProducerHostnameTopicKey(hostname, topicName), 1L, TimeUnit.HOURS);
                for (String msgType : msgTypes){
                    String key = buildProducerHostnameKey(hostname) + ":" + msgType;
                    redisTemplate.expire(key, 1L, TimeUnit.HOURS);
                }
            }
        }
        //收集消费者
        Set<String> consumerHostnames = getAllConsumerHostnames();
        for (String hostname : consumerHostnames){
            Set<String> msgTypes = redisTemplate.opsForSet().members(buildConsumerHostnameTopicKey(hostname, topicName));
            if (!CollectionUtils.isEmpty(msgTypes)){
                redisTemplate.expire(buildConsumerHostnameTopicKey(hostname, topicName), 1L, TimeUnit.HOURS);
                for (String msgType : msgTypes){
                    String key = buildConsumerHostnameKey(hostname) + ":" + msgType;
                    redisTemplate.expire(key, 1L, TimeUnit.HOURS);
                }
            }
        }

    }

    @Override
    public List<String> getAllAccumulatedTopicFromSet() {

        return new ArrayList<>(redisTemplate.opsForSet().members(ACCUMULATED_TOPIC));
    }


    /**
     * 添加生产者的主机名（放入一个set结构中）
     *
     * @param hostname
     */
    @Override
    public void addProducerHostName(String hostname) {

        //DATA_STAT_PRODUCER_SET
        redisTemplate.opsForSet().add(DATA_STAT_PRODUCER_SET, hostname);
    }

    /**
     * 统计生产者的数据
     * @param hostname
     * @param topic
     * @param producerCountMap key:消息类型 value：累计总数
     */
    @Override
    public void countProducerData(String hostname, String topic, Map<String, AtomicLong> producerCountMap) {

        if (producerCountMap == null){
            return;
        }

        //拼接key名
        /*
            key(String结构)： DATA_STAT_PRODUCER:Pod1:AA value:汇总数值
            DATA_STAT_PRODUCER:Pod1:AA -> 1000
            DATA_STAT_PRODUCER:Pod1:BB -> 1000
            DATA_STAT_PRODUCER:Pod1:CC -> 500
            key(set结构):
            DATA_STAT_PRODUCER:Pod1:Topic -> (AA、BB、CC)
         */
        String key = buildProducerHostnameTopicKey(hostname, topic);
        String key2 = buildProducerHostnameKey(hostname);

        producerCountMap.forEach((msgType, count)->{
            redisTemplate.opsForSet().add(key, msgType);
            redisTemplate.opsForValue().set(key2 + ":" + msgType, count);
        });

    }

    private String buildProducerHostnameKey(String hostname) {

        return DATA_STAT_PRODUCER + ":" + hostname;
    }

    private String buildConsumerHostnameKey(String hostname) {

        return DATA_STAT_CONSUMER + ":" + hostname;
    }

    private String buildProducerHostnameTopicKey(String hostname, String topic) {

        return DATA_STAT_PRODUCER + ":" + hostname + ":" + topic;
    }

    @Override
    public void sendHostnameHeartBeat(String hostname) {

        //必须设置过期时间
        redisTemplate.opsForValue().set(buildHostnamHeartbeatKey(hostname), hostname, 20, TimeUnit.SECONDS);
    }

    private String buildHostnamHeartbeatKey(String hostname) {

        return POD_HEARTBEAT + ":" + hostname;
    }

    @Override
    public void addConsumerHostName(String hostname) {

        redisTemplate.opsForSet().add(DATA_STAT_CONSUMER_SET, hostname);
    }

    @Override
    public void countConsumerData(String hostname, String topic, Map<String, AtomicLong> consumerCountMap) {

        if(consumerCountMap == null){
            return;
        }

        String key = buildConsumerHostnameTopicKey(hostname, topic);
        String key2 = buildConsumerHostnameKey(hostname);

        consumerCountMap.forEach((msgType, count)->{
            redisTemplate.opsForSet().add(key, msgType);
            redisTemplate.opsForValue().set(key2 + ":" + msgType, count);
        });
    }

    private String buildConsumerHostnameTopicKey(String hostname, String topic) {

        return DATA_STAT_CONSUMER + ":" + hostname + ":" + topic;
    }

    @Override
    public String collectData() {

        //遍历主题
        List<String> accumulatedTopics = getAllAccumulatedTopicFromSet();
        if (!CollectionUtils.isEmpty(accumulatedTopics)){

            //计算
            StringBuilder builder = new StringBuilder();
            for (String topic : accumulatedTopics){

                builder.append(collectDataByTopic(topic));
            }
            return builder.toString();
        }

        return null;
    }

    private String collectDataByTopic(String topic) {

        Map<String, AtomicLong> producerMap = new HashMap<>();
        Map<String, AtomicLong> consumerMap = new HashMap<>();

        //收集生产者
        //找到所有生产者hostname
        Set<String> producerHostnames = getAllProducerHostnames();
        for (String hostname : producerHostnames){
            String heartBeatKey = buildHostnamHeartbeatKey(hostname);
            //只处理存活的hostname
            if (hostNameAlive(heartBeatKey)){
                //
                Set<String> msgTypes = redisTemplate.opsForSet().members(buildProducerHostnameTopicKey(hostname, topic));
                if (!CollectionUtils.isEmpty(msgTypes)){
                    for (String msgType : msgTypes){
                        String key = buildProducerHostnameKey(hostname) + ":" + msgType;
                        AtomicLong value = (AtomicLong) redisTemplate.opsForValue().get(key);
                        if (value != null){
                            producerMap.put(msgType, value);
                        }
                    }
                }
            }
        }
        //收集消费者
        Set<String> consumerHostnames = getAllConsumerHostnames();
        for (String hostname : consumerHostnames){
            String heartBeatKey = buildHostnamHeartbeatKey(hostname);
            //只处理存活的hostname
            if (hostNameAlive(heartBeatKey)){
                //
                Set<String> msgTypes = redisTemplate.opsForSet().members(buildConsumerHostnameTopicKey(hostname, topic));
                if (!CollectionUtils.isEmpty(msgTypes)){
                    for (String msgType : msgTypes){
                        String key = buildConsumerHostnameKey(hostname) + ":" + msgType;
                        AtomicLong value = (AtomicLong) redisTemplate.opsForValue().get(key);
                        if (value != null){
                            consumerMap.put(msgType, value);
                        }
                    }
                }
            }
        }

        Map<String, Long> resultMap = new HashMap<>();
        producerMap.forEach((k,v)->{
            AtomicLong produceCount = producerMap.get(k);
            AtomicLong consumeCount = consumerMap.get(k);
            if (produceCount != null){
                if (consumeCount != null && (produceCount.get() - consumeCount.get()) > 0){
                    resultMap.put(k, produceCount.get() - consumeCount.get());
                }else if (consumeCount == null){
                    //假如采集不到消费者数据，可能消费者已经宕机了，但是生产仍在持续
                    resultMap.put(k, produceCount.get());
                }
            }
        });

        //排序
        if (!resultMap.isEmpty()){
            List<String> keyList = resultMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toList());
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("主题%s堆积情况:", topic));
            for (String key : keyList){
                builder.append(String.format("[消息类型：%s 堆积量：%s]", key, resultMap.get(key)) + "|");
            }
            String res = builder.toString();
            return res.substring(0, res.length()-1);
        }

        return "";

    }

    private boolean hostNameAlive(String heartBeatKey) {

        Object object = redisTemplate.opsForValue().get(heartBeatKey);
        return object != null && StringUtils.isNotEmpty((String)object);
    }

    private Set<String> getAllConsumerHostnames() {

        return redisTemplate.opsForSet().members(DATA_STAT_CONSUMER_SET);
    }

    private Set<String> getAllProducerHostnames() {

        return redisTemplate.opsForSet().members(DATA_STAT_PRODUCER_SET);
    }


}
