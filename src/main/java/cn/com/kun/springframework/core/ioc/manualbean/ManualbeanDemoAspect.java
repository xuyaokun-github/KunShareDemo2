package cn.com.kun.springframework.core.ioc.manualbean;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

//@Component
//@Aspect
public class ManualbeanDemoAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(ManualbeanDemoAspect.class);

    /**
     * 定义一个切入点 例子：cn.com.kun.controller这个包下的所有类的所有方法都会被切入
     */
    @Pointcut("execution(* cn.com.kun.springframework.core.ioc.manualbean.*.*(..))")
    public void pointcut() {}

    /*
     * 定义一个环绕通知
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        LOGGER.info("进入ManualbeanDemoAspect，method.getName:{}", method.getName());
        Object obj = pjp.proceed();
        return obj;
    }

}
