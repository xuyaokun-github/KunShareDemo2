package cn.com.kun.component.tthawk.dynamicaspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 动态切面
 *
 * author:xuyaokun_kzx
 * date:2024/7/22
 * desc:
 * 方案1：大范围拦截，尽可能将整个包的类都增强（这个方法是行得通的，但是不优雅，而且容易切出问题）
 * 方案2：动态放入切面
 *
 */
//@Component //假如加了这个注解，启动初期就已经初始化好切面了，不算在运行时才加入的（这也是一种方法，但是切面拦截的范围太大容易出其他问题）
//@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Aspect
public class TthawkDynamicAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkDynamicAspect.class);

    //方法1：大范围拦截
//    @Pointcut("execution(* cn.com.kun.service..*.*(..))")
//    @Pointcut("execution(* cn.com.kun.component.tthawk.dynamicaspect..*.*(..))")
    @Pointcut("execution(public * cn.com.kun.service.tthawk.TthawkThirdDemoService.doWork7(..))")
    public void pointCut(){

    }

    /**
     * 增强逻辑
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        LOGGER.info("进入TthawkDynamicAspect");
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();

        if (method.getName().contains("doWork")){
            //主动抛异常
            doThrowCustomException();
        }

        //原逻辑
        Object obj = pjp.proceed();
        return obj;
    }

    private void doThrowCustomException() {

        throw new RuntimeException("tthawk运行时异常");
    }

}



