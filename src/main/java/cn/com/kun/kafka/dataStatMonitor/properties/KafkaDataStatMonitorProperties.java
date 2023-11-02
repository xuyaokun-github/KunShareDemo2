package cn.com.kun.kafka.dataStatMonitor.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *
 * author:xuyaokun_kzx
 * date:2022/12/29
 * desc:
*/
@Component
@ConfigurationProperties(prefix ="kafka.data-stat")
public class KafkaDataStatMonitorProperties implements Serializable {

    private boolean collectReportEnabled;

    private boolean producerReportEnabled;

    private boolean consumerReportEnabled;

    private boolean lagCheckEnabled;

    /**
     * 正常的监控时间间隔：默认是1分钟
     * 假如用户定义了大于0的值，以用户定义的为准
     */
    private long normalTimeIntervalMs;

    private long dataCollectScheduledIntervalMs;

    public boolean isCollectReportEnabled() {
        return collectReportEnabled;
    }

    public void setCollectReportEnabled(boolean collectReportEnabled) {
        this.collectReportEnabled = collectReportEnabled;
    }

    public boolean isLagCheckEnabled() {
        return lagCheckEnabled;
    }

    public void setLagCheckEnabled(boolean lagCheckEnabled) {
        this.lagCheckEnabled = lagCheckEnabled;
    }

    public long getNormalTimeIntervalMs() {
        return normalTimeIntervalMs;
    }

    public void setNormalTimeIntervalMs(long normalTimeIntervalMs) {
        this.normalTimeIntervalMs = normalTimeIntervalMs;
    }

    public boolean isProducerReportEnabled() {
        return producerReportEnabled;
    }

    public void setProducerReportEnabled(boolean producerReportEnabled) {
        this.producerReportEnabled = producerReportEnabled;
    }

    public boolean isConsumerReportEnabled() {
        return consumerReportEnabled;
    }

    public void setConsumerReportEnabled(boolean consumerReportEnabled) {
        this.consumerReportEnabled = consumerReportEnabled;
    }

    public long getDataCollectScheduledIntervalMs() {
        return dataCollectScheduledIntervalMs;
    }

    public void setDataCollectScheduledIntervalMs(long dataCollectScheduledIntervalMs) {
        this.dataCollectScheduledIntervalMs = dataCollectScheduledIntervalMs;
    }
}
