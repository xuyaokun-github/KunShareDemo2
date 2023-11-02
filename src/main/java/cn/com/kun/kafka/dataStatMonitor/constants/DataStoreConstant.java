package cn.com.kun.kafka.dataStatMonitor.constants;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/10/26
 * desc:
*/
public class DataStoreConstant {

    /**
     * 已堆积主题
     */
    public static final String ACCUMULATED_TOPIC = "KAFKA_DATA_STAT:ACCUMULATED_TOPIC";

    /**
     * 节点心跳
     */
    public static final String POD_HEARTBEAT = "KAFKA_DATA_STAT:POD_HEARTBEAT";

    /**
     * 生产者主机名列表
     */
    public static final String DATA_STAT_PRODUCER_SET = "KAFKA_DATA_STAT:DATA_STAT_PRODUCER_SET";

    /**
     * 消费者主机名列表
     */
    public static final String DATA_STAT_CONSUMER_SET = "KAFKA_DATA_STAT:DATA_STAT_PRODUCER_SET";

    /**
     * 生产者数据统计前缀
     */
    public static final String DATA_STAT_PRODUCER = "KAFKA_DATA_STAT:DATA_STAT_PRODUCER";

    /**
     * 消费者数据统计前缀
     */
    public static final String DATA_STAT_CONSUMER = "KAFKA_DATA_STAT:DATA_STAT_CONSUMER";

}
