package cn.com.kun.springframework.springcloud.alibaba.sentinel.demo.kafkademo;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.component.sentinel.properties.SentinelRuleProperties;
import cn.com.kun.component.sentinel.vo.CustomFlowRule;
import cn.com.kun.kafka.speedControl.ConsumerThreadTypeEnum;
import cn.com.kun.kafka.speedControl.KafkaSpeedControlManager;
import cn.com.kun.kafka.speedControl.TopicResourceRelationHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.Map;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.CONTEXT_MSG_PUSH;

/**
 * 这个类模拟一个Kafka消费者线程
 *
 * author:xuyaokun_kzx
 * date:2021/10/8
 * desc:
*/
@ConditionalOnProperty(prefix = "kunsharedemo.kafka-speed-control-demo", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Service
public class KafkaSentinelThreadDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaSentinelThreadDemoService.class);

    @Autowired
    private KafkaSpeedControlManager kafkaSpeedControlManager;

    @Autowired
    private TopicResourceRelationHolder topicResourceRelationHolder;

    @Autowired
    private SentinelRuleProperties sentinelRuleProperties;

    //业务层
    @Autowired
    private KscInvokeDownStreamDemoService invokeDownStreamDemoService;


    @PostConstruct
    public void init(){

        //这一句很重要
        kafkaSpeedControlManager.registContext(CONTEXT_MSG_PUSH);

        //注册主题与待监听资源的关系
        //首先从主题名中切出具备业务含义的 SM04,
        //然后再拿着MSG_PUSH + SM04，看flowRuleMap里有没有，假如有则put进去，假如没有，则拼接MSG_PUSH + 平台类型，再put
        //这一系列行为都是上游完成
        Map<String, CustomFlowRule> flowRuleMap = sentinelRuleProperties.getFlowRule();
        topicResourceRelationHolder.put("MSG_CACHE_SM04", "MSG_PUSH_SPS");
        topicResourceRelationHolder.put("MSG_CACHE_SM04", "MSG_PUSH_SM01");

        //中优先级线程
//        startThread(ConsumerThreadTypeEnum.MIDDLE, RESOURCE_MSG_PUSH_SM01, "SM01", "SPS");

        //高优先级
//        startThread(ConsumerThreadTypeEnum.HIGH, RESOURCE_MSG_PUSH_SM01, "SM01", "SPS");

        //中优先级线程
        //没有针对SM04配置限流值，它会使用SPS对应的限流值，但是此时，资源名称就不能继续用SM01了，得用平台的限流值
        startThread(ConsumerThreadTypeEnum.MIDDLE, "MSG_CACHE_SM04", "SM01", "SPS");
    }

    private void startThread(ConsumerThreadTypeEnum consumerThreadTypeEnum, String topic, String channel, String platform) {

        new Thread(()->{

            while (true){
                try {
                    ThreadUtils.sleep(1000);
                    LOGGER.info("我是kafka消费线程{},当前时间：{}", Thread.currentThread().getName(), DateUtils.nowWithMillis());

                    //正常情况下，拉消息是一直拉，没有睡眠 或者是睡眠1秒
                    //假如触发了黄线或者红线就开始sleep
                    //模拟一个中优先级线程（这样一行代码不优雅 TODO）
                    kafkaSpeedControlManager.await(consumerThreadTypeEnum.getTypeName(), topic);

                    //业务逻辑调用下游
                    for (int i = 0; i < 5000; i++) {
                        invokeDownStreamDemoService.invokeDownStream(channel, platform);
                    }
                }catch (Exception e){
                    LOGGER.error("kafka消费线程异常", e);
                }
            }

        }, "KafkaSentinelThreadDemoService-" + String.join("-", topic, consumerThreadTypeEnum.getTypeName(), channel, platform) + "-Thread")
                .start();
    }


}
