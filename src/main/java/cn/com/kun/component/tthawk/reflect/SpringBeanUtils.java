package cn.com.kun.component.tthawk.reflect;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    public static Object getBean(String className, String springBeanName) {

        Object bean = null;
        Class beanClass = null;
        try {
            beanClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String[] beanNames = context.getBeanNamesForType(beanClass);
        if (beanNames != null && beanNames.length > 0){
            if (beanNames.length > 1){
                for (String beanName : beanNames) {
                    if (beanName.equals(springBeanName)){
                        bean = context.getBean(beanName);
                        break;
                    }
                }
            }else {
                bean = context.getBean(beanNames[0]);
            }
        }else {
            bean = context.getBean(springBeanName);
        }

        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtils.context = applicationContext;
    }

}
