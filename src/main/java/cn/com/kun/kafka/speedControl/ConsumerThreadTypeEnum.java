package cn.com.kun.kafka.speedControl;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/10/24
 * desc:
*/
public enum ConsumerThreadTypeEnum {

    /**
     * 消费者线程类型：高优先级线程
     */
    HIGH("HIGH"),
    /**
     * 消费者线程类型：中优先级线程
     */
    MIDDLE("MIDDLE"),
    /**
     * 消费者线程类型：低优先级线程
     */
    LOW("LOW");

    public String typeName;

    ConsumerThreadTypeEnum(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

}
