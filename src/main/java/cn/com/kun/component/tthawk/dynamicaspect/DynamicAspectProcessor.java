package cn.com.kun.component.tthawk.dynamicaspect;

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

    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * 注册切面
     *
     * @param beanName
     */
    public void registerAspect(String beanName) {
        TthawkDynamicAspect aspect = new TthawkDynamicAspect();
        context.getBeanFactory().registerSingleton(beanName, aspect);
        System.out.println("Aspect " + beanName + " registered.");
        context.getBeanFactory().getBean(beanName);
    }

    /**
     * 卸载切面
     *
     * @param beanName
     */
    public void unregisterAspect(String beanName) {
        if (context.containsBean(beanName)) {
            ((DefaultListableBeanFactory)context.getBeanFactory()).destroySingleton(beanName);
            System.out.println("Aspect " + beanName + " unregistered.");
        }
    }


    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {

        ((DefaultListableBeanFactory)context.getBeanFactory()).registerBeanDefinition(beanName, beanDefinition);
    }
}
