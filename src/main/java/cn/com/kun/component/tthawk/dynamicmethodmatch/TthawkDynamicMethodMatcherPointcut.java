package cn.com.kun.component.tthawk.dynamicmethodmatch;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * 动态方法匹配切点
 *
 * author:xuyaokun_kzx
 * date:2024/8/5
 * desc:
*/
public class TthawkDynamicMethodMatcherPointcut extends DynamicMethodMatcherPointcut {

    private String classPackage;

    public TthawkDynamicMethodMatcherPointcut(String classPackage) {
        this.classPackage = classPackage;
    }

    /**
     * （1）对类进行静态切点检查
     *
     */
    public ClassFilter getClassFilter() {

        return new ClassFilter() {

            @Override
            public boolean matches(Class<?> clazz) {

                String classname = clazz.getName();
                return classname.startsWith(classPackage);
            }
        };

    }

    /**
     * （2）对方法进行静态切点检查
     */

    @Override
    public boolean matches(Method method, Class<?> targetClass) {

        return true;
    }

    /**
     * （3）对方法进行动态切点检查
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {

        String methodKey = targetClass.getName() + "." + method.getName();
        return DynamicMethodMatchHolder.contains(methodKey);
    }
}
