package cn.com.kun.springframework.springcloud.feign.extend.localinvoke;

import org.apache.commons.lang3.StringUtils;
import org.apache.groovy.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用方法
 * factories文件要新增：
 * org.springframework.boot.SpringApplicationRunListener=\
 * cn.com.kun.springframework.springcloud.feign.extend.localinvoke.FeignClientApplicationRunListener
 * 还不如用FeignClientsLocalInvokeBeanPostProcessor
 *
 * author:xuyaokun_kzx
 * date:2024/9/25
 * desc:
*/
public class FeignClientApplicationRunListener implements InstantiationAwareBeanPostProcessor, SpringApplicationRunListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientApplicationRunListener.class);

    private Environment environment;

    /**
     * 默认的 url
     */
    private static final String defaultUrl = "http://xxxxx.com/";

    /**
     * 指定值
     * 比如指定哪个 contextId 的 url 为多少
     */
    private static final Map<String, String> specialMap = Maps.of(
            "contextId1", "url1"
            , "contextId2", "url2"
    );

    /**
     * 根据名称过滤
     */
    private static final String BEAN_NAME_FLAG = "FeignClientFactoryBean";

    public FeignClientApplicationRunListener() {
//        System.out.println("进入FeignClientApplicationRunListener无参构造函数");
    }

    public FeignClientApplicationRunListener(Environment environment) {
        this.environment = environment;
    }

    /**
     * 这个方法不能删除（框架反射创建监听器时会用到）
     *
     * @param application
     * @param args
     */
    public FeignClientApplicationRunListener(SpringApplication application, String[] args) {

    }


    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {

        if (!(BEAN_NAME_FLAG.equals(bean.getClass().getSimpleName())) && !bean.toString().contains("HardCodedTarget")){
            //假如不是feign的代理类和FeignClientFactoryBean无需处理
            return pvs;
        }

        String beanToString = bean.toString();
        String feignclientName = extractFeignClientName(beanToString);
        String configUrl = environment.getProperty(String.format("feign.localinvoke.url.%s", feignclientName));
        boolean matchTargetFeign = StringUtils.isNotEmpty(configUrl);
        if (matchTargetFeign){
            //开始修改url
            modifyUrl(bean, configUrl);
            LOGGER.info("feignclient：[{}], 采用本地调试url:[{}], 原url:{}", feignclientName, configUrl);
        }else {
            //没匹配，不做修改
            LOGGER.info("feignclient：[{}]无需修改url", feignclientName);
        }

        //modifyFeignClient(beanName, pvs);

        return pvs;
    }

    private String extractFeignClientName(String toString) {

        Pattern pattern = Pattern.compile("name=(.*?),");
        Matcher matcher = pattern.matcher(toString);
        while(matcher.find()) {
            //只输出匹配的串
            return matcher.group(1);
        }

        return "";
    }

    private void modifyUrl(Object bean, String configUrl) {

        try {
            Field h = bean.getClass().getSuperclass().getDeclaredField("h");
            h.setAccessible(true);
            Object aopProxy =  h.get(bean);

            Field arg$3 = aopProxy.getClass().getDeclaredField("arg$3");
            arg$3.setAccessible(true);

            Object innetType = arg$3.get(aopProxy);

            Field url = innetType.getClass().getDeclaredField("url");
            url.setAccessible(true);
            url.set(innetType, configUrl);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void modifyFeignClient(String beanName, PropertyValues propertyValues) {
        try {

            PropertyValue nameProperty = propertyValues.getPropertyValue("name");
            if (Objects.isNull(nameProperty)) {
                return;
            }
            PropertyValue contextIdProperty = propertyValues.getPropertyValue("contextId");
            if (Objects.isNull(contextIdProperty) || Objects.isNull(contextIdProperty.getValue())) {
                return;
            }

            String contextId = contextIdProperty.getValue().toString();
            PropertyValue urlProperty = propertyValues.getPropertyValue("url");
            if (Objects.nonNull(urlProperty) && Objects.nonNull(urlProperty.getValue()) && StringUtils.isNotBlank((String)urlProperty.getValue())) {
                LOGGER.info(">>>>>>>>>>>>> {} 已经存在 url 属性 = {}，故不进行 url 的设置", contextId, urlProperty.getValue());
                return;
            }

            //没有url的进行设置
            String special = specialMap.getOrDefault(contextId, "");
            String urlVal = StringUtils.isNotBlank(special) ? special : defaultUrl;

            if (Objects.nonNull(urlProperty)) {
                urlProperty.setConvertedValue(urlVal);
                LOGGER.info(">>>>>>>>>>>>> {} 塞 url = {}", contextId, urlVal);
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>>>>  @FeignClient 进行 url 设置报错", e);
        }
    }


    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

        String enabled = environment.getProperty("feign.localinvoke.beanPostProcessor.enabled");
        if ("true".equals(enabled)){
            LOGGER.info("增加BeanPostProcessor[FeignClientApplicationRunListener]");
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            beanFactory.addBeanPostProcessor(new FeignClientApplicationRunListener(environment));
        }

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


    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        this.environment = environment;
    }


}
