package cn.com.kun.springframework.core.eventListener;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Spring事件机制工具类
 *
 * author:xuyaokun_kzx
 * date:2024/3/8
 * desc:
*/
@Component
public class SpringEventUtil implements InitializingBean, ApplicationContextAware {

    private static SpringEventUtil instance = null;

    private static ApplicationContext context;

    public static void publishEvent(Object event) {

        checkApplicationContext();
        context.publishEvent(event);
    }

    private static void checkApplicationContext() {
        Assert.notNull(context,
                "applicationContext未注入,请在applicationContext.xml中定义SpringContextUtil");
    }

    public static SpringEventUtil getInstance() {
        return instance;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
