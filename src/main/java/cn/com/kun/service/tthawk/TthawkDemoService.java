package cn.com.kun.service.tthawk;

import cn.com.kun.service.switchcheck.SwitchCheckerDemoService;
import cn.com.kun.service.tthawk.vo.TthawkDemoVO1;
import cn.com.kun.service.tthawk.vo.TthawkDemoVO2;
import cn.com.kun.springframework.springcloud.feign.client.KunwebdemoFeign2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TthawkDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SwitchCheckerDemoService.class);

    @Autowired
    private TthawkSecondDemoService tthawkSecondDemoService;

    @Resource
//    @Autowired
    private TthawkThirdDemoService tthawkThirdDemoService;


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

    public void test5() {

        try {
            System.out.println(tthawkThirdDemoService);
            tthawkThirdDemoService.doWork5();
        }catch (Exception e){
            LOGGER.error("调用TthawkSecondDemoService执行异常", e);
        }
    }

    public void test6() {

        try {
//            ResultVo resultVo = kunwebdemoFeign2.result1("");
//            LOGGER.info("kunweb返回:{}", JacksonUtils.toJSONString(resultVo));

            tthawkThirdDemoService.doWork6();
        }catch (Exception e){
            LOGGER.error("调用tthawkThirdDemoService业务层test6执行异常", e);
        }
    }

    public void test66() {

        try {
            tthawkThirdDemoService.doWork6();
        }catch (Exception e){
            //像这种代码，不好覆盖了（spring会擅自封装方法上没有声明的异常）
            //只能用改写法，参考下面的 test662()
            if (e instanceof InterruptedException){
                LOGGER.error("调用业务层出现中断异常", e);
            }else {
                LOGGER.error("调用tthawkThirdDemoService业务层test6执行异常", e);
            }
        }
    }


    public void test662() {

        try {
            tthawkThirdDemoService.doWork6();
        }catch (Exception e){
            exceptionHandle(e);
        }
    }

    /**
     * 像这个方法就能用第一只鹰的第一个攻击方法覆盖到
     * @param e
     */
    private void exceptionHandle(Exception e) {
        if (e instanceof InterruptedException){
            LOGGER.error("调用业务层出现中断异常", e);
        }else {
            LOGGER.error("调用tthawkThirdDemoService业务层test6执行异常", e);
        }
    }


}
