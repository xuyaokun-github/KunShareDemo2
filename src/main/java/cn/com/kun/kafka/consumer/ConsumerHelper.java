package cn.com.kun.kafka.consumer;

import cn.com.kun.common.utils.ThreadUtils;
import org.springframework.stereotype.Component;

@Component
public class ConsumerHelper {

    private boolean consumeSlow;

    public void sleep() {

        if (consumeSlow){
            ThreadUtils.sleep(1000);//模拟一个消费慢的场景
        }
    }


    public void consumeFast() {

        consumeSlow = false;
    }


    public void consumeSlow() {

        consumeSlow = true;

    }

    public boolean isConsumeSlow() {
        return consumeSlow;
    }

}
