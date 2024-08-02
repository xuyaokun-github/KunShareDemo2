package cn.com.kun.component.tthawk.dynamicpointcut;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TthawkInterceptor implements MethodInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        LOGGER.info("进入TthawkInterceptor");
        return methodInvocation.proceed();
    }
}
