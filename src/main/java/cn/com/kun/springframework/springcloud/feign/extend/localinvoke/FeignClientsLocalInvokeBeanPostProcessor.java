package cn.com.kun.springframework.springcloud.feign.extend.localinvoke;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OpenFeign扩展--本地调用Bean扩展
 *
 * 功能：支持通过开关控制是否启用本地调试模式，仅支持OpenFeign，不支持Feign(工厂bean的class类型：org.springframework.cloud.openfeign.FeignClientFactoryBean)
 * 原理：设置了url,feign就不会再通过注册中心的方式找目标地址
 *
 * 使用注意事项
 * 1.feignclient的起名必须包含feign字样
 *
 *
 * author:xuyaokun_kzx
 * date:2021/11/17
 * desc:
*/
public class FeignClientsLocalInvokeBeanPostProcessor implements BeanPostProcessor, EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientsLocalInvokeBeanPostProcessor.class);

    private Environment environment;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        String beanToString = null;
        try {
            //有些scope作用域的bean调用toString方法会报错，为了规避这个错，加一个约束: feignclient的类名必须含有feign或者Feign才进行增强
            if (beanName.toLowerCase().contains("feign")){
                beanToString = bean.toString();
            }
        }catch (Exception e){
            LOGGER.warn("{}调用toString方法异常", beanName, e);
        }

        //feign.Target.HardCodedTarget
        if (beanToString != null && beanToString.contains("HardCodedTarget")){
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
        }

        return bean;
    }

    private void modifyUrl(Object bean, String configUrl) {

        try {
            Field h = bean.getClass().getSuperclass().getDeclaredField("h");
            h.setAccessible(true);
            Object aopProxy =  h.get(bean);


            Field[] fields = aopProxy.getClass().getDeclaredFields();
            if (existArg3(fields)){
                doModifyUrl(aopProxy, configUrl);
            }else {
                //说明被二次代理
                String name = aopProxy.getClass().getName();
                if (name.equals("org.springframework.aop.framework.JdkDynamicAopProxy")){
                    Field advised = aopProxy.getClass().getDeclaredField("advised");
                    advised.setAccessible(true);
                    Object advisedObj = advised.get(aopProxy);
                    if (advisedObj instanceof ProxyFactory){
                        Object targetSourceObj = ((ProxyFactory) advisedObj).getTargetSource();
                        if (targetSourceObj instanceof SingletonTargetSource){
                            Object target = ((SingletonTargetSource) targetSourceObj).getTarget();
                            h = target.getClass().getSuperclass().getDeclaredField("h");
                            h.setAccessible(true);
                            aopProxy = h.get(target);
                            if (existArg3(aopProxy.getClass().getDeclaredFields())){
                                doModifyUrl(aopProxy, configUrl);
                            }
                        }
                    }


                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean existArg3(Field[] fields) {

        if (fields != null && fields.length > 0){
            for (Field field : fields){
                if (field.getName().equals("arg$3")){
                    return true;
                }
            }
        }

        return false;
    }

    private void doModifyUrl(Object aopProxy, String configUrl) {

        try {
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


    private String extractFeignClientName(String toString) {

        Pattern pattern = Pattern.compile("name=(.*?),");
        Matcher matcher = pattern.matcher(toString);
        if (matcher.find()){
            //只输出匹配的串
            return matcher.group(1);
        }
        return "";
    }

    /**
     * TODO
     * 不支持Feign(工厂bean的class类型：org.springframework.cloud.openfeign.FeignClientFactoryBean)
     * 原理是默认情况下是url属性是空值，将它改成本地指定的地址，就不会走注册中心
     *
     * @param clazz
     * @param obj
     * @throws Exception
     */
    private  void changeUrl(Class clazz, Object obj) throws Exception{

        Field nameField = ReflectionUtils.findField(clazz, "name");
        if(Objects.nonNull(nameField)){
            ReflectionUtils.makeAccessible(nameField);
            String name = (String) nameField.get(obj);
            if (StringUtils.isNotEmpty(name)){
                Field field = ReflectionUtils.findField(clazz, "url");
                if(Objects.nonNull(field)){
                    ReflectionUtils.makeAccessible(field);
                    //旧url
                    Object value = field.get(obj);
                    String newUrl = getLocalInvokeUrlFromConfig(name);
                    if (StringUtils.isNotEmpty(newUrl)){
                        //假如配置了本地调用url,则替换
                        ReflectionUtils.setField(field, obj, newUrl);
                        LOGGER.info("feignBeanName：[{}], 采用本地调试url:[{}], 原url:{}", name, newUrl, value);
                    }
                }
            }
        }
    }

    private String getLocalInvokeUrlFromConfig(String name) {

        //例子：feign.localinvoke.url.kunwebdemo=http://127.0.0.1:8091
        return environment.getProperty(String.format("feign.localinvoke.url.%s", name));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
