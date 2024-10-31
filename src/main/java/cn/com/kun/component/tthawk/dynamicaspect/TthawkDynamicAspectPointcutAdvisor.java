package cn.com.kun.component.tthawk.dynamicaspect;

import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 基于类名+方法名匹配切入点
 *
 * author:xuyaokun_kzx
 * date:2024/10/31
 * desc:
 */
public class TthawkDynamicAspectPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {


    @Override
    public boolean matches(Method method, Class<?> targetClass) {

        String classname = targetClass.getName();
        Set<String> methodSet = TthawkDynamicAspectClassMethodHolder.getMethodSet(classname);
        return methodSet != null && methodSet.contains(method.getName());
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
                return TthawkDynamicAspectClassMethodHolder.containsClass(className);
            }
        };
    }


    @Override
    public Advice getAdvice() {

        TthawkDynamicAspectIntroductionInterceptor interceptor = new TthawkDynamicAspectIntroductionInterceptor();
        return interceptor;
    }

}