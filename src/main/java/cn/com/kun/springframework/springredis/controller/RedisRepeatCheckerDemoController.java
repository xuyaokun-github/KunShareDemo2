package cn.com.kun.springframework.springredis.controller;


import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springredis.repeatcheck.RedisRepeatChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RequestMapping("/spring-redis-repeatChecker")
@RestController
public class RedisRepeatCheckerDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisRepeatCheckerDemoController.class);

    @Autowired
    RedisRepeatChecker redisRepeatChecker;


    @GetMapping(value = "/test")
    public ResultVo test(){

        String uuid = UUID.randomUUID().toString();
//        LOGGER.info("{}", redisRepeatChecker.check(uuid));
//        LOGGER.info("{}", redisRepeatChecker.check(uuid));

        return ResultVo.valueOfSuccess("");
    }

    @GetMapping(value = "/testRepeat")
    public ResultVo testRepeat(){

        String uuid = UUID.randomUUID().toString();
//        LOGGER.info("{}", redisRepeatChecker.check(uuid));
//        LOGGER.info("{}", redisRepeatChecker.check(uuid));

        int count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            new Thread(()->{

                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //可以自由定义次数
                if (redisRepeatChecker.check(uuid, 1, 60, TimeUnit.SECONDS)){
                    //执行业务逻辑
                    LOGGER.info("执行业务逻辑：{}", Thread.currentThread().getName());
                }else {
                    LOGGER.info("触发防重！！！：{}", Thread.currentThread().getName());
                }

            }).start();
        }

        return ResultVo.valueOfSuccess("");
    }

}
