package cn.com.kun.springframework.core.ioc.thirdCache;

import org.springframework.beans.factory.annotation.Autowired;

//@Service
public class ThirdCacheDemoBService {

    @Autowired
    ThirdCacheDemoAService thirdCacheDemoAService;

    public void method(){

    }

}
