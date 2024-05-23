package cn.com.kun.controller.hccounter;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.hccounter.HccounterDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@RequestMapping("/hccounter")
@RestController
public class HccounterDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(HccounterDemoController.class);

    @Autowired
    private HccounterDemoService hccounterDemoService;

    /**
     *
     * @return
     */
    @GetMapping("/test")
    public ResultVo<Integer> test() throws InterruptedException {

        //
        String uuid = UUID.randomUUID().toString();
        //开启10个线程，分别计数 100万次，每计数1万次，随机睡眠100-500ms
        hccounterDemoService.init(uuid);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {

            new Thread(()->{
                for (int j = 0; j < 100; j++) {
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextLong(500));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int k = 0; k < 10000; k++) {
                        hccounterDemoService.add(uuid);
                    }
                }
                LOGGER.info("{}计数结束", Thread.currentThread().getName());
                countDownLatch.countDown();
            }, "hcc-thread-" + i).start();

        }

        countDownLatch.await();
        Thread.sleep(15 * 1000);
        LOGGER.info("最终累计总数：{}", hccounterDemoService.getTotal(uuid));

        return ResultVo.valueOfSuccess();
    }




}
