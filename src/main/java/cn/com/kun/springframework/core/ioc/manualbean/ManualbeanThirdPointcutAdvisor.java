package cn.com.kun.springframework.core.ioc.manualbean;

import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.reflect.Method;

/**
 * 基于类名+方法名匹配切入点
 * 例子：
 * 只增强 cn.com.kun.springframework.core.ioc.manualbean.ManualFirstService.method2
 *
 * author:xuyaokun_kzx
 * date:2024/10/31
 * desc:
 */
public class ManualbeanThirdPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {

    /**
     *
     */
    private static String METHOD_NAME = "method2";

    private static String CLASS_NAME = "cn.com.kun.springframework.core.ioc.manualbean.ManualFirstService";

    /**
     * 静态方法匹配判断，这里只有方法名为sayHello的，才能被匹配
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return METHOD_NAME.equals(method.getName());
    }

    /**
     * 覆盖getClassFilter，只匹配Dog类
     * @return
     */
    public ClassFilter getClassFilter() {
        return new ClassFilter() {
            @Override
            public boolean matches(Class<?> clazz) {
                String className = clazz.getName();
                //ManualFirstService.class.isAssignableFrom(clazz);
                return CLASS_NAME.equals(className);
            }
        };
    }


    @Override
    public Advice getAdvice() {

        ManualbeanThirdIntroductionInterceptor interceptor = new ManualbeanThirdIntroductionInterceptor();
        return interceptor;
    }

}