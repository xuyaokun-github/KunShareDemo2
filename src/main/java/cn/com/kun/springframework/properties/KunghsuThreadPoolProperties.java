package cn.com.kun.springframework.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 *
 * author:xuyaokun_kzx
 * date:2022/12/29
 * desc:
*/
@Component
@ConfigurationProperties(prefix ="kunghsu.thread-pool")
public class KunghsuThreadPoolProperties implements Serializable {

    private String topicPrefix;

    private boolean consumerEnabled;

    private boolean producerEnabled;

    @PostConstruct
    public void init(){
        this.toString();
    }

    public String getTopicPrefix() {
        return topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public String getDefaultTopic() {
        return topicPrefix + "_DEFAULT";
    }


    public boolean isConsumerEnabled() {
        return consumerEnabled;
    }

    public void setConsumerEnabled(boolean consumerEnabled) {
        this.consumerEnabled = consumerEnabled;
    }

    public boolean isProducerEnabled() {
        return producerEnabled;
    }

    public void setProducerEnabled(boolean producerEnabled) {
        this.producerEnabled = producerEnabled;
    }

}
