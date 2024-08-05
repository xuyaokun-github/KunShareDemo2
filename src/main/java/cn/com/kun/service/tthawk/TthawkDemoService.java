package cn.com.kun.service.tthawk;

import cn.com.kun.service.switchcheck.SwitchCheckerDemoService;
import cn.com.kun.springframework.springcloud.feign.client.KunwebdemoFeign2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TthawkDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SwitchCheckerDemoService.class);

    @Autowired
    private TthawkSecondDemoService tthawkSecondDemoService;

    @Autowired
    private KunwebdemoFeign2 kunwebdemoFeign2;

    public void test() {

        try {
            tthawkSecondDemoService.doWork();
        }catch (Exception e){
            //通常就是因为一两行进不去，影响了最终的精准测试覆盖率
            LOGGER.error("调用TthawkSecondDemoService出现异常", e);
            //exception record
            exceptionRecord();
        }

    }

    public void test2(TthawkDemoVO1 tthawkDemoVO1, TthawkDemoVO2 tthawkDemoVO2) {

        if (tthawkDemoVO2 == null){
            LOGGER.info("tthawkDemoVO2是空");
            LOGGER.info("tthawkDemoVO1：{}", tthawkDemoVO1.getName());
        }

        try {
            tthawkSecondDemoService.doWork1();
            tthawkSecondDemoService.doWork2();
        }catch (Exception e){
            LOGGER.error("调用TthawkSecondDemoService执行异常", e);
        }

    }


    public void test3() {

        try {
            //调用feign
//            kunwebdemoFeign2.result1("");

            tthawkSecondDemoService.doWork3();

        }catch (Exception e){
            LOGGER.error("调用TthawkSecondDemoService执行异常", e);
        }

    }


    private void exceptionRecord() {
        LOGGER.info("执行异常现场记录动作");
    }

    public void test4() {


        try {

            tthawkSecondDemoService.doWork4();
        }catch (Exception e){
            LOGGER.error("调用TthawkSecondDemoService执行异常", e);
        }

    }
}
