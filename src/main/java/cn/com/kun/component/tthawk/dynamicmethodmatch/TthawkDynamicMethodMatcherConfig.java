package cn.com.kun.component.tthawk.dynamicmethodmatch;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/8/1
 * desc:
*/
@ConditionalOnProperty(prefix = "tthawk.dmm", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Configuration
public class TthawkDynamicMethodMatcherConfig {

    /**
     * 不建议将这个包设置得太大
     */
    @Value("${tthawk.dmm.classPackage:cn.com.kun.component.tthawk.dynamicmethodmatch}")
    private String classPackage;

    @Bean
    public Pointcut customDynamicMethodMatcherPointcut() {

        TthawkDynamicMethodMatcherPointcut pointcut = new TthawkDynamicMethodMatcherPointcut(classPackage);
        return pointcut;
    }

    @Bean
    public TthawkDynamicMethodMatchBeforeAdvice tthawkDynamicMethodMatchBeforeAdvice() {

        return new TthawkDynamicMethodMatchBeforeAdvice();
    }

    @Bean
    public DefaultPointcutAdvisor defaultDynamicMethodMatcherPointcutAdvisor() {

        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setPointcut(customDynamicMethodMatcherPointcut());
        defaultPointcutAdvisor.setAdvice(tthawkDynamicMethodMatchBeforeAdvice());
        return defaultPointcutAdvisor;
    }

}
