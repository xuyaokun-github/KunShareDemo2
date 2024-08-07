package cn.com.kun.component.tthawk.dynamicproxy;

import cn.com.kun.component.tthawk.dynamicpointcut.FeignHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/8/1
 * desc:
*/
public class TthawkDynamicProxyBeforeAdvice implements MethodBeforeAdvice{

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkDynamicProxyBeforeAdvice.class);

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
        if(DynamicProxyKeyHolder.contains(methodKey)){
            Object obj = buildException(DynamicProxyKeyHolder.getExceptionClass(methodKey));
            LOGGER.info("主动抛出异常：{}", obj.getClass().getName());
            throw (Throwable) obj;
        }
    }

    private Object buildException(String exceptionClass) {

        Class clazz = null;
        Object sourceBean = null;
        try {
            clazz = Class.forName(exceptionClass);
            sourceBean = clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return sourceBean;
    }

}
