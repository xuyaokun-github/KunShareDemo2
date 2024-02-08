package cn.com.kun.common.listener;

import cn.com.kun.common.utils.DesedeUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.AbstractApplicationContext;

public class CustomContextInitializedListener implements ApplicationListener<ApplicationContextInitializedEvent>, BeanDefinitionRegistryPostProcessor {

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        AbstractApplicationContext context = (AbstractApplicationContext) event.getApplicationContext();
        context.addBeanFactoryPostProcessor(this);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if(!registry.containsBeanDefinition("desedeUtils")){
            registry.registerBeanDefinition("desedeUtils", new RootBeanDefinition(DesedeUtils.class));
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
