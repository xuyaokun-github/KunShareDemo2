package cn.com.kun.kafka.speedControl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *
 * author:xuyaokun_kzx
 * date:2021/10/11
 * desc:
*/
@Component
@ConfigurationProperties(prefix ="kafka-consumer-speed")
public class KafkaConsumerSpeedProperties implements Serializable {

    /**
     * 开关：是否启用中优先级和低优先级线程
     */
    private boolean enabled;

    private String highSleepTimeWhenRed;

    private String highSleepTimeWhenYellow;

    private String highSleepTimeWhenGreen;

    private String middleSleepTimeWhenRed;

    private String middleSleepTimeWhenYellow;

    private String middleSleepTimeWhenGreen;

    private String lowSleepTimeWhenRed;

    private String lowSleepTimeWhenYellow;

    private String lowSleepTimeWhenGreen;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHighSleepTimeWhenRed() {
        return highSleepTimeWhenRed;
    }

    public void setHighSleepTimeWhenRed(String highSleepTimeWhenRed) {
        this.highSleepTimeWhenRed = highSleepTimeWhenRed;
    }

    public String getHighSleepTimeWhenYellow() {
        return highSleepTimeWhenYellow;
    }

    public void setHighSleepTimeWhenYellow(String highSleepTimeWhenYellow) {
        this.highSleepTimeWhenYellow = highSleepTimeWhenYellow;
    }

    public String getHighSleepTimeWhenGreen() {
        return highSleepTimeWhenGreen;
    }

    public void setHighSleepTimeWhenGreen(String highSleepTimeWhenGreen) {
        this.highSleepTimeWhenGreen = highSleepTimeWhenGreen;
    }

    public String getMiddleSleepTimeWhenRed() {
        return middleSleepTimeWhenRed;
    }

    public void setMiddleSleepTimeWhenRed(String middleSleepTimeWhenRed) {
        this.middleSleepTimeWhenRed = middleSleepTimeWhenRed;
    }

    public String getMiddleSleepTimeWhenYellow() {
        return middleSleepTimeWhenYellow;
    }

    public void setMiddleSleepTimeWhenYellow(String middleSleepTimeWhenYellow) {
        this.middleSleepTimeWhenYellow = middleSleepTimeWhenYellow;
    }

    public String getMiddleSleepTimeWhenGreen() {
        return middleSleepTimeWhenGreen;
    }

    public void setMiddleSleepTimeWhenGreen(String middleSleepTimeWhenGreen) {
        this.middleSleepTimeWhenGreen = middleSleepTimeWhenGreen;
    }

    public String getLowSleepTimeWhenRed() {
        return lowSleepTimeWhenRed;
    }

    public void setLowSleepTimeWhenRed(String lowSleepTimeWhenRed) {
        this.lowSleepTimeWhenRed = lowSleepTimeWhenRed;
    }

    public String getLowSleepTimeWhenYellow() {
        return lowSleepTimeWhenYellow;
    }

    public void setLowSleepTimeWhenYellow(String lowSleepTimeWhenYellow) {
        this.lowSleepTimeWhenYellow = lowSleepTimeWhenYellow;
    }

    public String getLowSleepTimeWhenGreen() {
        return lowSleepTimeWhenGreen;
    }

    public void setLowSleepTimeWhenGreen(String lowSleepTimeWhenGreen) {
        this.lowSleepTimeWhenGreen = lowSleepTimeWhenGreen;
    }
}
