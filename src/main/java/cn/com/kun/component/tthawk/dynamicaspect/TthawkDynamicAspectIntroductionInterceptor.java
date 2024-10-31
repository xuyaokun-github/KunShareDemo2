package cn.com.kun.component.tthawk.dynamicaspect;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.component.tthawk.core.MethodKeyExceptionHolder;
import cn.com.kun.component.tthawk.core.NestedExceptionHelper;
import cn.com.kun.springframework.core.ioc.manualbean.ManualbeanThirdIntroductionInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.lang.reflect.Method;

/**
 * 通知实现类
 * author:xuyaokun_kzx
 * date:2021/10/26
 * desc:
*/
public class TthawkDynamicAspectIntroductionInterceptor implements IntroductionInterceptor, BeanFactoryAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(ManualbeanThirdIntroductionInterceptor.class);

    /**
     * 这里就是处理实际被代理的逻辑，在前后织入代理逻辑
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        LOGGER.info("进入TthawkDynamicAspectIntroductionInterceptor，time:{} method:{}", DateUtils.now(), invocation.getMethod().getName());

        Method method = invocation.getMethod();
        String methodKey = invocation.getMethod().getDeclaringClass().getName() + "." + method.getName();

        //是否需要主动抛出异常
        if(MethodKeyExceptionHolder.contains(methodKey)){

            Object obj = null;
            String nestedExceptionClass = MethodKeyExceptionHolder.getNestedExceptionClass(methodKey);
            if (nestedExceptionClass != null){
                obj = NestedExceptionHelper.buildNestedException(nestedExceptionClass);
            }else {
                obj = NestedExceptionHelper.buildException(MethodKeyExceptionHolder.getExceptionClass(methodKey));
            }

            if (obj != null){
                LOGGER.info("抛出异常：{}", obj.getClass().getName());
                throw (Throwable) obj;
            }
        }

        //被代理逻辑
        Object result = invocation.proceed();

        return result;
    }

    @Override
    public boolean implementsInterface(Class<?> intf) {
        return false;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }
}
