package cn.com.kun.component.tthawk.dynamicmethodmatch;

import cn.com.kun.component.tthawk.core.FeignHelper;
import cn.com.kun.component.tthawk.core.NestedExceptionHelper;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.adapter.AdvisorAdapter;

import java.lang.reflect.Method;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/8/1
 * desc:
*/
public class TthawkDynamicMethodMatchBeforeAdvice implements MethodBeforeAdvice, AfterReturningAdvice, AdvisorAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkDynamicMethodMatchBeforeAdvice.class);

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {

        // 输出切点
        String methodKey = target.getClass().getName() + "." + method.getName();

        //假如是被代理的对象，例如feign之类的（适用于11.10版本）
        if (FeignHelper.isFeignProxy(target)){
            //代理类
            LOGGER.info("识别到feign代理类");
            //第一种方法，取到Feign类的类名
            String feignClientClassName = FeignHelper.getFeignClientClassName(target);
            methodKey = feignClientClassName + "." + method.getName();
        }

        //是否需要主动抛出异常
        if(DynamicMethodMatchHolder.contains(methodKey)){
            Object obj = NestedExceptionHelper.buildException(DynamicMethodMatchHolder.getExceptionClass(methodKey));
            if(obj != null){
                LOGGER.info("抛出异常：{}", obj.getClass().getName());
                throw (Throwable) obj;
            }
        }
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
    }

    @Override
    public boolean supportsAdvice(Advice advice) {
        return true;
    }

    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        return null;
    }

}
