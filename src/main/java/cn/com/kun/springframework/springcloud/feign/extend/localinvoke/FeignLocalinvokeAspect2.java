package cn.com.kun.springframework.springcloud.feign.extend.localinvoke;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 测试用
 * 假如想测试feign类被二次代理，打开这个类
 *
 * author:xuyaokun_kzx
 * date:2024/9/25
 * desc:
*/
@Component
@Aspect
public class FeignLocalinvokeAspect2 {

    private final static Logger LOGGER = LoggerFactory.getLogger(FeignLocalinvokeAspect2.class);

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping(){

    }

    @Around(value = "getMapping()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        Object obj = pjp.proceed();
        return obj;
    }


}
