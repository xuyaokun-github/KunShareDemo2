package cn.com.kun.kafka.speedControl;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.sentinel.flowmonitor.SentinelFlowMonitor;
import cn.com.kun.component.sentinel.vo.FlowMonitorRes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Kafka消费线程管理器
 * 作用：通过让Kafka线程睡眠，从而控制Kafka线程消费速度
 * 依赖了sentinel扩展组件
 *
 * author:xuyaokun_kzx
 * date:2021/10/8
 * desc:
*/
@Component
public class KafkaSpeedControlManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaSpeedControlManager.class);

    /**
     *  配置文件
     */
    @Autowired
    private KafkaConsumerSpeedProperties kafkaConsumerSpeedProperties;

    @Autowired
    private TopicResourceRelationHolder topicResourceRelationHolder;

    /**
     * 强依赖 sentinel扩展组件
     */
    @Autowired
    private SentinelFlowMonitor sentinelFlowMonitor;


    @PostConstruct
    public void init(){

        LOGGER.info("kafkaConsumerSpeedProperties：{}", JacksonUtils.toJSONString(kafkaConsumerSpeedProperties));
    }

    /**
     * 供上层调用
     *
     * @param contextName
     */
    public void registContext(String contextName){

        sentinelFlowMonitor.registContextName(contextName);
    }

    

    /**
     * 假如需要控制消费线程速度，可以在一批消息执行完之后调用该方法
     * 进入睡眠等待N秒，然后开始下一次拉取
     *
     * @param consumerThreadType 消费者类型
     * @param topic 主题名
     * @throws InterruptedException
     */
    public void await(String consumerThreadType, String topic) throws InterruptedException {

        if (!kafkaConsumerSpeedProperties.isEnabled()){
            //开关未开启
            return;
        }

        /*
         * 需要监听哪些资源，根据业务决定
         * 通过主题名，能找到待监听的限流资源列表
         */
        String[] resources = topicResourceRelationHolder.get(topic);
        if (resources == null || resources.length < 1){
            //假如上层未注册，则直接return
            return;
        }
        /*
            监听一个资源或者多个资源，由上层决定
         */
        FlowMonitorRes flowMonitorRes = sentinelFlowMonitor.getMergeFlowMonitorRes(resources);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("flowMonitorRes：{}", JacksonUtils.toJSONString(flowMonitorRes));
        }
        if (flowMonitorRes != null){
            //计算睡眠时间
            long sleepTime = calculateWaitTime(flowMonitorRes, consumerThreadType);
            if (sleepTime > 0){
                LOGGER.info("{}开始睡眠{}ms", Thread.currentThread().getName(), sleepTime);
                Thread.sleep(sleepTime);
            }
        }

    }

    /**
     * 计算睡眠时间
     * @param flowMonitorRes
     * @param consumerThreadType
     * @return
     */
    private long calculateWaitTime(FlowMonitorRes flowMonitorRes, String consumerThreadType){

        String time = "";

        if(ConsumerThreadTypeEnum.HIGH.getTypeName().equals(consumerThreadType)){
            /*
             * 决定睡眠多久，可以灵活设置
             */
            if (flowMonitorRes.getRedFlag().get()){
                time = kafkaConsumerSpeedProperties.getHighSleepTimeWhenRed();
            }else if (flowMonitorRes.getYellowFlag().get()){
                time = kafkaConsumerSpeedProperties.getHighSleepTimeWhenYellow();
            } else {
                time = kafkaConsumerSpeedProperties.getHighSleepTimeWhenGreen();
            }
        }else if (ConsumerThreadTypeEnum.MIDDLE.getTypeName().equals(consumerThreadType)){
            /*
             * 决定睡眠多久，可以灵活设置
             */
            if (flowMonitorRes.getRedFlag().get()){
                time = kafkaConsumerSpeedProperties.getMiddleSleepTimeWhenRed();
            }else if (flowMonitorRes.getYellowFlag().get()){
                time = kafkaConsumerSpeedProperties.getMiddleSleepTimeWhenYellow();
            } else {
                time = kafkaConsumerSpeedProperties.getMiddleSleepTimeWhenGreen();
            }
        }else if (ConsumerThreadTypeEnum.LOW.getTypeName().equals(consumerThreadType)){
            /*
             * 决定睡眠多久，可以灵活设置
             */
            if (flowMonitorRes.getRedFlag().get()){
                time = kafkaConsumerSpeedProperties.getLowSleepTimeWhenRed();
            }else if (flowMonitorRes.getYellowFlag().get()){
                time = kafkaConsumerSpeedProperties.getLowSleepTimeWhenYellow();
            } else {
                time = kafkaConsumerSpeedProperties.getLowSleepTimeWhenGreen();
            }
        }

        //也可以根据具体的QPS值决定该睡多久,可以制定一个公式，灵活判断 TODO
//        flowMonitorRes.getTotalQps();

        return StringUtils.isEmpty(time) ? 0L : Long.valueOf(time);
    }

}
