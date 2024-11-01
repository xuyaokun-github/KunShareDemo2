package cn.com.kun.service.tthawk.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/7/22
 *
*/
@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class TthawkDemoAspect2 {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkDemoAspect2.class);

    @Pointcut("execution(public * cn.com.kun.service.tthawk.TthawkSecondDemoService.doWork3(..))")
    public void pointCut(){

    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        LOGGER.info("进入TthawkDemoAspect2");
        //原逻辑
        Object obj = pjp.proceed();
        return obj;
    }

}
