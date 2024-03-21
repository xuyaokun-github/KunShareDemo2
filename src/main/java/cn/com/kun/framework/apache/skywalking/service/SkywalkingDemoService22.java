package cn.com.kun.framework.apache.skywalking.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class SkywalkingDemoService22 {

    public void method4() throws InterruptedException {

        Thread.sleep(ThreadLocalRandom.current().nextLong(1000));
    }
}
