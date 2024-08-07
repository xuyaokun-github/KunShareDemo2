package cn.com.kun.component.tthawk.dynamicaspect;

import org.aopalliance.aop.Advice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/8/5
 * desc:
*/
public class AspectModifier {

    public static void modifyAspect(ApplicationContext context, Class<?> beanClass, Class<?> newAspectClass) throws Exception {

        //获取Spring容器
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        ClassLoader classLoader = context.getClassLoader();
        String[] beanNames = context.getBeanNamesForType(beanClass);
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            //假如一个bean已经被代理过
            if (bean instanceof ProxyFactory){
                //获取目标Bean的代理工厂
                ProxyFactory proxyFactory = (ProxyFactory) bean;
                //设置新的切面类
                proxyFactory.addAdvice((Advice) newAspectClass.newInstance());
                //重新创建代理对象
                Object newProxy = proxyFactory.getProxy(classLoader);
                //替换原有的代理对象
                beanFactory.registerSingleton(beanName, newProxy);
            }else {
                //假如一个bean没被代理过，这里就是原生的对象
                ProxyFactory proxyFactory = new ProxyFactory(bean);
                proxyFactory.addAdvice(beforeAdvice());
                //重新创建代理对象
                Object newProxy = proxyFactory.getProxy(classLoader);
                //替换原有的代理对象
                beanFactory.destroySingleton(beanName);
                beanFactory.removeBeanDefinition(beanName);
                beanFactory.registerSingleton(beanName, newProxy);
            }

        }
    }


    public static Advice beforeAdvice() {

        MethodBeforeAdvice advice = new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, @Nullable Object target) throws Throwable {
                System.out.println("准备调用：" + method);
            }
        };
        return advice;
    }

}

