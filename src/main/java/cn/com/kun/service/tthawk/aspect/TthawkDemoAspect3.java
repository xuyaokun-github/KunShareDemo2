package cn.com.kun.service.tthawk.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/7/22
 *
*/
//@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
//@Aspect
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class TthawkDemoAspect3 {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkDemoAspect3.class);

    @Pointcut("execution(public * cn.com.kun.service.tthawk.TthawkThirdDemoService.doWork6(..))")
    public void pointCut(){

    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        LOGGER.info("进入TthawkDemoAspect3");
        //原逻辑
        Object obj = pjp.proceed();
        return obj;
    }

}
