package cn.com.kun.component.tthawk.dynamicaspect;

import cn.com.kun.component.tthawk.core.TthawkSpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 这个方法没生效
 *
 * author:xuyaokun_kzx
 * date:2024/7/22
 * desc:
*/
@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Service
public class DynamicAspectProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(DynamicAspectProcessor.class);

    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * 注册切面
     *
     * @param beanName
     */
    public void registerAspect(String beanName) {

        //1.
//        TthawkDynamicAspect aspect = new TthawkDynamicAspect();
//        context.getBeanFactory().registerSingleton(beanName, aspect);
//        LOGGER.info("注册切面完成，{}", beanName);

        //2.

        TthawkSpringBeanFactory.setBeanDefinition("tthawkDynamicAspect", TthawkDynamicAspect.class);

        //尝试获取一下bean
        Object bean = TthawkSpringBeanFactory.getBean("tthawkDynamicAspect");

        //假如希望后续创建bean发现刚加进去的增强器，需要刷新org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper.cachedAdvisorBeanNames
        //得用反射做这个事情
        TthawkSpringBeanFactory.refreshCachedAdvisorBeanNames();

    }


    /**
     * 卸载切面
     *
     * @param beanName
     */
    public void unregisterAspect(String beanName) {
        if (context.containsBean(beanName)) {
            ((DefaultListableBeanFactory)context.getBeanFactory()).destroySingleton(beanName);
            LOGGER.info("Aspect {} unregistered", beanName);
        }
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {

        ((DefaultListableBeanFactory)context.getBeanFactory()).registerBeanDefinition(beanName, beanDefinition);
    }

    public void registerAdvisor(String beanName, Class<?> clazz) {

        TthawkSpringBeanFactory.setBeanDefinition(beanName, clazz);

        //尝试获取一下bean
        Object bean = TthawkSpringBeanFactory.getBean(beanName);

        //假如希望后续创建bean发现刚加进去的增强器，需要刷新org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper.cachedAdvisorBeanNames
        //得用反射做这个事情
        TthawkSpringBeanFactory.refreshCachedAdvisorBeanNames();
    }



}
