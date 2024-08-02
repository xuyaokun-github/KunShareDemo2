package cn.com.kun.component.tthawk.dynamicpointcut;

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
@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Configuration
public class TthawkDynamicPointcutConfig {

    @Value("${tthawk.executionExpr:}")
    private String executionExpression;

    @Bean
    public Pointcut customPointCut() {

        String expression = "execution(public * cn.com.kun.component.tthawk.reflect.TthawkSecondController.attack(..))";

        if (executionExpression != null && executionExpression.length() > 0){
            expression = executionExpression;
        }
        TthawkDynamicPointcut greetingDynamicPointcut = new TthawkDynamicPointcut();
        greetingDynamicPointcut.setExpression(expression);
        return greetingDynamicPointcut;
    }

    @Bean
    public TthawkBeforeAdvice getAdvice() {

        return new TthawkBeforeAdvice();
    }

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor() {

        DefaultPointcutAdvisor defaultPointcutAdvisor = new
                        DefaultPointcutAdvisor();

        defaultPointcutAdvisor.setPointcut(customPointCut());
        defaultPointcutAdvisor.setAdvice(getAdvice());
        return defaultPointcutAdvisor;
    }

}
