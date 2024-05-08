package cn.com.kun.springframework.core.aop.dynamicAop;


import cn.com.kun.springframework.SpringDemoController;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class LogMethodInterceptor implements MethodInterceptor {

    private final static Logger log = LoggerFactory.getLogger(SpringDemoController.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result;
        try {
            result = invocation.proceed();
        } finally {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>TargetClass:【{}】,method:【{}】,args:【{}】",invocation.getThis().getClass().getName(),invocation.getMethod().getName(), Arrays.toString(invocation.getArguments()));
        }

        return result;
    }
}
