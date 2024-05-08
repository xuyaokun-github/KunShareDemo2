package cn.com.kun.springframework.core.aop.dynamicAop;

import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * TODO  未完成
 *
 * author:xuyaokun_kzx
 * date:2024/4/30
 * desc:
*/
public class AopUtil {

    private final static Logger log = LoggerFactory.getLogger(AopUtil.class);

    private static final String PROXY_PLUGIN_PREFIX = "";

    public static void registerProxy(DefaultListableBeanFactory beanFactory, ProxyMetaInfo proxyMetaInfo){
        AspectJExpressionPointcutAdvisor advisor = getAspectJExpressionPointcutAdvisor(beanFactory, proxyMetaInfo);
        addOrDelAdvice(beanFactory, OperateEventEnum.ADD, advisor);
    }
    
    public static void destoryProxy(DefaultListableBeanFactory beanFactory, String id){
        String beanName = PROXY_PLUGIN_PREFIX + id;
        if(beanFactory.containsBean(beanName)){
            AspectJExpressionPointcutAdvisor advisor = beanFactory.getBean(beanName,AspectJExpressionPointcutAdvisor.class);
            addOrDelAdvice(beanFactory,OperateEventEnum.DEL,advisor);
            beanFactory.destroyBean(beanFactory.getBean(beanName));
        }
    }
    
    private static AspectJExpressionPointcutAdvisor getAspectJExpressionPointcutAdvisor(DefaultListableBeanFactory beanFactory, ProxyMetaInfo proxyMetaInfo) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
        GenericBeanDefinition beanDefinition = (GenericBeanDefinition) builder.getBeanDefinition();
        beanDefinition.setBeanClass(AspectJExpressionPointcutAdvisor.class);
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(proxyMetaInfo.getPointcut());
        advisor.setAdvice(Objects.requireNonNull(getMethodInterceptor(proxyMetaInfo.getProxyUrl(), proxyMetaInfo.getProxyClassName())));
        beanDefinition.setInstanceSupplier((Supplier<AspectJExpressionPointcutAdvisor>) () -> advisor);
        beanFactory.registerBeanDefinition(PROXY_PLUGIN_PREFIX + proxyMetaInfo.getId(),beanDefinition);

        return advisor;
    }

    /**
     * 传入的是jar包的url
     *
     * @param proxyUrl
     * @param proxyClassName
     * @return
     */
    private static Advice getMethodInterceptor(String proxyUrl, Object proxyClassName) {

        return null;
    }


    public static void addOrDelAdvice(DefaultListableBeanFactory beanFactory, OperateEventEnum operateEventEnum,AspectJExpressionPointcutAdvisor advisor){
        AspectJExpressionPointcut pointcut = (AspectJExpressionPointcut) advisor.getPointcut();
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            Object bean = beanFactory.getBean(beanDefinitionName);
            if(!(bean instanceof Advised)){
                if(operateEventEnum == OperateEventEnum.ADD){
                    buildCandidateAdvised(beanFactory,advisor,bean,beanDefinitionName);
                }
                continue;
            }
            Advised advisedBean = (Advised) bean;
            boolean isFindMatchAdvised = findMatchAdvised(advisedBean.getClass(), pointcut);
            if(operateEventEnum == OperateEventEnum.DEL){
                if(isFindMatchAdvised){
                    advisedBean.removeAdvice(advisor.getAdvice());
                    log.info("Remove Advice -->【{}】 For Bean -->【{}】 SUCCESS !",
                            advisor.getAdvice().getClass().getName(), bean.getClass().getName());
                }
            }else if(operateEventEnum == OperateEventEnum.ADD){
                if(isFindMatchAdvised){
                    advisedBean.addAdvice(advisor.getAdvice());
                    log.info("Add Advice -->【{}】 For Bean -->【{}】 SUCCESS !",
                            advisor.getAdvice().getClass().getName(), bean.getClass().getName());
                }
            }


        }
    }

    private static boolean findMatchAdvised(Class<? extends Advised> aClass, AspectJExpressionPointcut pointcut) {

        return pointcut.matches(aClass);
    }


    private static void buildCandidateAdvised(DefaultListableBeanFactory beanFactory, AspectJExpressionPointcutAdvisor advisor, Object bean, String beanDefinitionName) {

        //TODO

    }


}
