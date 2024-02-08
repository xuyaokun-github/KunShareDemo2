package cn.com.kun.common.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 支持springboot2.7.X版本
 *
 * author:xuyaokun_kzx
 * date:2024/2/8
 * desc:
*/
public class MyBeanLoadInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyBeanLoadInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        // 注意，如果你同时还使用了 spring cloud，这里需要做个判断，要不要在 spring cloud applicationContext 中做这个事
        // 通常 spring cloud 中的 bean 都和业务没关系，是需要跳过的
        if (!isSpringCloudContext(applicationContext)){
            applicationContext.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());
        }

    }

    /**
     * 判断一个上下文是否为springcloud context
     * @param applicationContext
     * @return
     */
    private boolean isSpringCloudContext(ConfigurableApplicationContext applicationContext) {

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Object object = environment.getPropertySources() == null ? null : environment.getPropertySources().get("bootstrap");
        return applicationContext.getParent() == null && object != null;
    }



}