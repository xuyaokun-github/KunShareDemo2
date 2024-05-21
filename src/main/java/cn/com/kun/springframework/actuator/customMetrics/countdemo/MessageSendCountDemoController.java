package cn.com.kun.springframework.actuator.customMetrics.countdemo;

import cn.com.kun.common.vo.ResultVo;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务监控demo
 * 统计每天的消息发送量
 */
@RequestMapping("/customMetrics-message-send")
@RestController
public class MessageSendCountDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageSendCountDemoController.class);

    @Autowired
    private MeterRegistry meterRegistry;

    private boolean countEnabled = true;

    @GetMapping("/start-counter-thread")
    public ResultVo startCounterThread() throws Exception {

        Counter counter = Counter.builder("my_message_send")
                .tag("templateCode", "BCP001")
                .tag("sendChannel", "EM01")
                .register(meterRegistry);
        Counter counter2 = Counter.builder("my_message_send")
                .tag("templateCode", "BCP001")
                .tag("sendChannel", "EM02")
                .register(meterRegistry);

        new Thread(()->{

            while (true){

                if (countEnabled){
                    counter2.increment();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{

            while (true){
                counter.increment();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return ResultVo.valueOfSuccess("OK");
    }


    @GetMapping("/close-count")
    public ResultVo closeCount() throws Exception {

        countEnabled = false;

        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/start-count")
    public ResultVo startCount() throws Exception {

        countEnabled = true;

        return ResultVo.valueOfSuccess();
    }



}
