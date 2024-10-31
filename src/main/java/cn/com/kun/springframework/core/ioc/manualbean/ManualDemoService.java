package cn.com.kun.springframework.core.ioc.manualbean;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 这是一个即将通过手动的方式放入spring容器的bean
 * 类上不用加任何注解。
 * @Autowired之类的正常用，仍会继续生效
 *
 * author:xuyaokun_kzx
 * date:2024/10/29
 * desc:
*/
public class ManualDemoService {

    @Autowired
    private ManualFirstService manualFirstService;

    @ManualbeanTimeLog
    public void method() {

        manualFirstService.method1();
    }

}
