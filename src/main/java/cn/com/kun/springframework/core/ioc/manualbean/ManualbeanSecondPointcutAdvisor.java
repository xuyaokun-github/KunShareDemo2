package cn.com.kun.springframework.core.ioc.manualbean;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * 基于注解匹配切入点
 *
 * author:xuyaokun_kzx
 * date:2024/10/31
 * desc:
*/
public class ManualbeanSecondPointcutAdvisor extends AbstractPointcutAdvisor {

    @Override
    public Pointcut getPointcut() {

        //spring提供的工具包：org.springframework.aop.support.annotation.AnnotationMatchingPointcut
        return AnnotationMatchingPointcut.forMethodAnnotation(ManualbeanTimeLog.class);
    }

    @Override
    public Advice getAdvice() {

        ManualbeanTimeLogIntroductionInterceptor interceptor = new ManualbeanTimeLogIntroductionInterceptor();
        return interceptor;
    }


}