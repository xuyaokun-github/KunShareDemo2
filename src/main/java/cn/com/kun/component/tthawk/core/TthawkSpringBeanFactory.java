package cn.com.kun.component.tthawk.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

/**
 * 自定义操作bean
 *
 * author:xuyaokun_kzx
 * date:2024/10/30
 * desc:
*/
@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Component
public class TthawkSpringBeanFactory implements BeanFactoryAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkSpringBeanFactory.class);

    private static DefaultListableBeanFactory listableBeanFactory;

    /**
     * 刷新org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper.cachedAdvisorBeanNames
     * 目的：让bean的创建过程能识别到动态加入的切面
     */
    public static void refreshCachedAdvisorBeanNames() {

        //得先用反射找到BeanFactoryAdvisorRetrievalHelper对象
        Map<String, InstantiationAwareBeanPostProcessor> beans = listableBeanFactory.getBeansOfType(InstantiationAwareBeanPostProcessor.class);
        if (beans != null && !beans.isEmpty()){

            InstantiationAwareBeanPostProcessor annotationAwareAspectJAutoProxyCreator = beans.get("org.springframework.aop.config.internalAutoProxyCreator");
            if (annotationAwareAspectJAutoProxyCreator != null && annotationAwareAspectJAutoProxyCreator instanceof AnnotationAwareAspectJAutoProxyCreator){
                AnnotationAwareAspectJAutoProxyCreator autoProxyCreator = (AnnotationAwareAspectJAutoProxyCreator) annotationAwareAspectJAutoProxyCreator;
                try {
                    if (autoProxyCreator instanceof AbstractAdvisorAutoProxyCreator){
                        AbstractAdvisorAutoProxyCreator abstractAdvisorAutoProxyCreator = (AbstractAdvisorAutoProxyCreator) autoProxyCreator;
                        //BeanFactoryAdvisorRetrievalHelper对象属性
                        Field advisorRetrievalHelperField = abstractAdvisorAutoProxyCreator.getClass().getSuperclass().getSuperclass().getDeclaredField("advisorRetrievalHelper");
                        advisorRetrievalHelperField.setAccessible(true);
                        Object advisorRetrievalHelper = advisorRetrievalHelperField.get(abstractAdvisorAutoProxyCreator);
                        if (advisorRetrievalHelper instanceof BeanFactoryAdvisorRetrievalHelper){
                            BeanFactoryAdvisorRetrievalHelper helper = (BeanFactoryAdvisorRetrievalHelper) advisorRetrievalHelper;
                            Field cachedAdvisorBeanNamesField = helper.getClass().getSuperclass().getDeclaredField("cachedAdvisorBeanNames");
                            cachedAdvisorBeanNamesField.setAccessible(true);
                            //将cachedAdvisorBeanNames重新置成空
                            cachedAdvisorBeanNamesField.set(helper, null);
                            LOGGER.info("将cachedAdvisorBeanNames重新置为空");
                        }
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 重建Bean(针对单例bean进行重建)
     *
     * @param beanName
     */
    public static void rebuildBean(String beanName) {

        /*
            重建bean关键点：
            需要重新触发依赖这个bean的其他bean的属性注入，这样才能保证新bean能被其他地方使用到
            所以这里必须先找出当前bean被哪些bean依赖。
            org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getDependentBeans
            org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getDependenciesForBean
         */
        //依赖了beanName的其他bean
        String[] dependentBeans = listableBeanFactory.getDependentBeans(beanName);
        //beanNamey依赖的所有bean
        //String[] dependenciesForBean = listableBeanFactory.getDependenciesForBean(beanName);

        if (dependentBeans != null && dependentBeans.length > 0){
            for (String name : dependentBeans){
                TthawkSpringBeanFactory.resetDepentBeanInjectionMetadataCache(name);
            }
        }

        /*
            重建bean的正确姿势不是移除bean定义，可能会导致依赖它的bean创建失败
         */
        //移除Bean定义（重建bean的反例）
//        listableBeanFactory.removeBeanDefinition(beanName);

        //根据beanID移除单例（重建bean的正例）
        listableBeanFactory.destroySingleton(beanName);

//        //这个也可以，先找到bean，再销毁bean
//        Object bean = listableBeanFactory.getBean(beanName);
//        if (bean != null){
//            listableBeanFactory.destroyBean(bean);
//        }

    }

    /**
     * 重建一个bean，不需要移除bean定义
     * （这样做不是不行，只是没必要。移除bean定义的过程中也会移除bean单例，并销毁bean）
     *
     * @param beanName
     * @param clazz
     */
    public static void rebuildBean2(String beanName, Class<?> clazz) {

        //1.移除bean定义
        listableBeanFactory.removeBeanDefinition(beanName);
        //2.重新注册bean定义
        BeanDefinition beanDefinition =  new RootBeanDefinition();
        beanDefinition.setBeanClassName(clazz.getName());
        //设置作用域
        beanDefinition.setScope(SCOPE_SINGLETON);
        listableBeanFactory.registerBeanDefinition(beanName, beanDefinition);
        //3.尝试获取bean，触发spring创建bean的过程
        listableBeanFactory.getBean(beanName);
    }


    public static void removeAdvisedBeans(String beanName) {

        Map<String, InstantiationAwareBeanPostProcessor> beans = listableBeanFactory.getBeansOfType(InstantiationAwareBeanPostProcessor.class);
        if (beans != null && !beans.isEmpty()){

            InstantiationAwareBeanPostProcessor annotationAwareAspectJAutoProxyCreator = beans.get("org.springframework.aop.config.internalAutoProxyCreator");
            if (annotationAwareAspectJAutoProxyCreator != null && annotationAwareAspectJAutoProxyCreator instanceof AnnotationAwareAspectJAutoProxyCreator){
                AnnotationAwareAspectJAutoProxyCreator autoProxyCreator = (AnnotationAwareAspectJAutoProxyCreator) annotationAwareAspectJAutoProxyCreator;
                try {
                    if (autoProxyCreator instanceof AbstractAutoProxyCreator){
                        AbstractAutoProxyCreator abstractAutoProxyCreator = autoProxyCreator;
                        Field advisedBeansField = abstractAutoProxyCreator.getClass().getSuperclass().getSuperclass()
                                .getSuperclass().getDeclaredField("advisedBeans");
                        advisedBeansField.setAccessible(true);
                        Map<Object, Boolean> advisedBeans = (Map<Object, Boolean>) advisedBeansField.get(abstractAutoProxyCreator);
                        advisedBeans.remove(beanName);
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 主动移除InjectionMetadataCache缓存（让bean A的创建过程重新注入最新的bean B）
     * @param beanName
     */
    public static void resetDepentBeanInjectionMetadataCache(String beanName) {

        //AutowiredAnnotationBeanPostProcessor
        Map<String, InstantiationAwareBeanPostProcessor> beans = listableBeanFactory.getBeansOfType(InstantiationAwareBeanPostProcessor.class);
        if (beans != null && !beans.isEmpty()){

            InstantiationAwareBeanPostProcessor beanPostProcessor = beans.get("org.springframework.context.annotation.internalAutowiredAnnotationProcessor");
            if (beanPostProcessor != null && beanPostProcessor instanceof AutowiredAnnotationBeanPostProcessor){
                AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = (AutowiredAnnotationBeanPostProcessor) beanPostProcessor;
                autowiredAnnotationBeanPostProcessor.resetBeanDefinition(beanName);
            }
        }
    }

    public static Object getBean(String beanName) {

        return listableBeanFactory.getBean(beanName);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)beanFactory;
        this.listableBeanFactory = listableBeanFactory;
    }

    public static void setBean(String beanName, Object object){
        //注意这里放入的bean在容器中是单例的
        listableBeanFactory.registerSingleton(beanName, object);
    }

    //根据beanName销毁(删除)单例的bean
    public static void removeSingletonBean(String beanName){
        listableBeanFactory.destroySingleton(beanName);
    }

    //手动把对象放入容器中,但是可以设置作用域
    public static void setBeanDefinition(String beanName, Class<?> clazz){

        BeanDefinition beanDefinition =  new RootBeanDefinition();
        beanDefinition.setBeanClassName(clazz.getName());
        //设置作用域
        beanDefinition.setScope(SCOPE_SINGLETON);
        listableBeanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    //根据beanName删除使用BeanDefinition创建的bean , Spring默认就是使用BeanDefinition创建的bean对象
    public static void removeBean(String beanName){
        listableBeanFactory.removeBeanDefinition(beanName);
    }


}
