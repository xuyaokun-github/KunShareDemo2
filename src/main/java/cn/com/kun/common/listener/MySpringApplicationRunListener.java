package cn.com.kun.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * 可以使用该监听器，诊断启动耗时
 * 必须放到resources 文件下的 META-INF/spring.factories   才能生效
 *
 * author:xuyaokun_kzx
 * date:2023/3/31
 * desc:
*/
@Component
public class MySpringApplicationRunListener implements SpringApplicationRunListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MySpringApplicationRunListener.class);

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        LOGGER.info("MySpringApplicationRunListener starting");
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        LOGGER.info("MySpringApplicationRunListener environmentPrepared");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        LOGGER.info("MySpringApplicationRunListener contextPrepared");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        LOGGER.info("MySpringApplicationRunListener contextLoaded");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }
}
