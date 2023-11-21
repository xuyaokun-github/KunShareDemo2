package cn.com.kun.springframework.core.ioc.beanoverride;

import org.springframework.stereotype.Component;

@Component("SpringBeanOverrideDemoService2")
public class SpringBeanOverrideDemoService2 implements SpringBeanOverrideDemoServiceBase {

    public String name() {
        return "SpringBeanOverrideDemoService2";
    }

}
