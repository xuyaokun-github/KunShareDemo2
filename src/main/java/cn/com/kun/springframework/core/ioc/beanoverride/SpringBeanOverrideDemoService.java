package cn.com.kun.springframework.core.ioc.beanoverride;

import org.springframework.stereotype.Component;

@Component("SpringBeanOverrideDemoService")
public class SpringBeanOverrideDemoService implements SpringBeanOverrideDemoServiceBase {


    public String name() {
        return "SpringBeanOverrideDemoService";
    }

}
