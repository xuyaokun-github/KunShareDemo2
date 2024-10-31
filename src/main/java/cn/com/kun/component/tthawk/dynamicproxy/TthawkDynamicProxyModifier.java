package cn.com.kun.component.tthawk.dynamicproxy;

import cn.com.kun.component.tthawk.core.AopTargetUtil;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * 基于spring aop创建代理
 *
 * author:xuyaokun_kzx
 * date:2024/8/5
 * desc:
*/
public class TthawkDynamicProxyModifier {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkDynamicProxyModifier.class);

    /**
     * 也支持feign client的代理
     *
     * @param context
     * @param beanClassName
     * @throws Exception
     */
    public static void proxy(ApplicationContext context, String beanClassName) throws Exception {

        Class beanClass = null;
        try {
            beanClass = Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(beanClass == null){
            return;
        }

        //获取Spring容器
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        ClassLoader classLoader = context.getClassLoader();
        String[] beanNames = context.getBeanNamesForType(beanClass);
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            Object newProxy = null;

            //假如一个bean没被代理过，这里就是原生的对象
            if (bean instanceof ProxyFactory){
                LOGGER.info("识别到ProxyFactory对象");
            }else if(bean.getClass().getName().contains("EnhancerBySpringCGLIB")){
                LOGGER.info("源对象是cglib代理对象");
                //第一版生成代理逻辑
                //因为是重新生成代理对象，这样做会丢失一些植入逻辑(注意，用完之后需要重启应用，将原有的增强逻辑还原)
                Object source = AopTargetUtil.getTarget(bean);
                ProxyFactory proxyFactory = new ProxyFactory(source);
                proxyFactory.addAdvice(beforeAdvice());
                newProxy = proxyFactory.getProxy(classLoader);

//                Object source = AopTargetUtil.getTarget(bean);
//                ProxyFactory proxyFactory = new ProxyFactory(bean);
//                  //proxyFactory.setInterfaces(source.getClass());//假如source不是基于接口的，这里用setInterfaces会报错
//                proxyFactory.setTargetClass(source.getClass());
//                proxyFactory.addAdvice(beforeAdvice());
//                newProxy = proxyFactory.getProxy(classLoader);

            }else {
                ProxyFactory proxyFactory = new ProxyFactory(bean);
                proxyFactory.addAdvice(beforeAdvice());
                //重新创建代理对象
                newProxy = proxyFactory.getProxy(classLoader);
            }

            if (newProxy != null){
                //替换原有的代理对象
                beanFactory.destroySingleton(beanName);

                boolean containsBeanDefinition = beanFactory.containsBeanDefinition(beanName);
                if (containsBeanDefinition){
                    try {
                        beanFactory.removeBeanDefinition(beanName);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                beanFactory.registerSingleton(beanName, newProxy);
            }
        }

    }


    public static Advice beforeAdvice() {

        MethodBeforeAdvice advice = new TthawkDynamicProxyBeforeAdvice();
        return advice;
    }

    public static void removeProxy(ApplicationContext context, String beanClassName) {

        Class beanClass = null;
        try {
            beanClass = Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(beanClass == null){
            return;
        }
        //获取Spring容器
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        String[] beanNames = context.getBeanNamesForType(beanClass);
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            beanFactory.destroySingleton(beanName);
            try {
                beanFactory.removeBeanDefinition(beanName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

