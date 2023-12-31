package cn.com.kun.springframework.springkafka.producer;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.kafka.msg.MsgCacheTopicMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;


/**
 * 消息生产者
 *
 * @author xuyaokun
 * @date 2020/3/16 11:44
 */
@Component
public class MyKafkaProducer {

    private static Logger logger = LoggerFactory.getLogger(MyKafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //发送消息方法
    public void sendOne() {

        String message = buildMessage();
                //"kunghsu msg................" + DateUtils.now();
        logger.info("发送消息 ----->>>>>  message = {}", message);
        //主题是hello。这个主题，不需要存在，假如不存在，会进行创建。但是默认创建的分区只有一个
        kafkaTemplate.send("hello-topic", message);
        //send方法有很多重载，可以指定key和分区
    }

    public void sendOneByGet() throws ExecutionException, InterruptedException {

        String message = buildMessage();
        //"kunghsu msg................" + DateUtils.now();
        logger.info("发送消息 ----->>>>>  message = {}", message);
        //主题是hello。这个主题，不需要存在，假如不存在，会进行创建。但是默认创建的分区只有一个
        //send方法有很多重载，可以指定key和分区
        //get方法，可以将异步转同步
        SendResult sendResult = kafkaTemplate.send("hello-topic", message).get();
        logger.info("发送消息sendResult：{}", sendResult);


    }

    private String buildMessage() {
        MsgCacheTopicMsg msgCacheTopicMsg = new MsgCacheTopicMsg();
        msgCacheTopicMsg.setStatusCode("" + ThreadLocalRandom.current().nextInt(10));
        msgCacheTopicMsg.setCreateTIme(new Date());
        msgCacheTopicMsg.setMsgId("" + System.currentTimeMillis());
        return JacksonUtils.toJSONString(msgCacheTopicMsg);
    }

    public void sendBatch() {

        for(int i=0;i < 10;i++){
            String message = buildMessage();
            logger.info("发送消息 ----->>>>>  message = {}", message);
            //主题是hello。这个主题，不需要存在，假如不存在，会进行创建。但是默认创建的分区只有一个
            kafkaTemplate.send("hello-topic", message);
            //send方法有很多重载，可以指定key和分区
        }

    }

    /**
     * 发送大报文，验证是否抛异常
     */
    public void sendLargeMsgAndTestException() {

        //制造一个超大报文，模拟kafka写入异常
        StringBuilder builder = new StringBuilder("kunghsu");
        int bytes = "kunghsu".getBytes().length;//字节数
        //5M的报文
        int totalBytes = 5 * 1024 * 1024;
        for (int i = 0; i < (totalBytes/bytes); i++) {
            builder.append("kunghsu");
        }
        String source = builder.toString();

        try {
            /*
                第1种方式
                org.springframework.kafka.support.LoggingProducerListener 会输出error日志，但是没有抛异常
             */
            ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send("hello-topic", source);

            /*
                第2种方式
                org.springframework.kafka.support.LoggingProducerListener 会输出error日志，get方法会抛异常
             */
            //即使定义了监听器，出现异常时只要调用了get方法，就会将异常抛出
//            ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send("hello-topic", source);
            SendResult<String, String> sendResult = listenableFuture.get();
//            listenableFuture.toString();

        }catch (Exception e){
            logger.error("Kafka发送异常", e);
        }

    }


    public void sendSmallMsgAndTestException() {

        //制造一个超大报文，模拟kafka写入异常
        StringBuilder builder = new StringBuilder("kunghsu");
        String source = builder.toString();

        try {
            /*
                第1种方式
                org.springframework.kafka.support.LoggingProducerListener 会输出error日志，但是没有抛异常
             */
            ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send("hello-topic", source);
            //回调器可以添加多个
            listenableFuture.addCallback(new MyProducerCallback());
            listenableFuture.addCallback(new MyProducerCallback2());

            /*
                第2种方式
                org.springframework.kafka.support.LoggingProducerListener 会输出error日志，get方法会抛异常
             */
//            ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send("hello-topic", source);
//            SendResult<String, String> sendResult = listenableFuture.get();
//            listenableFuture.toString();

        }catch (Exception e){
            logger.error("Kafka发送异常", e);
        }

    }
}