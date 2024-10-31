package cn.com.kun.component.tthawk.dynamicaspect;

import org.springframework.context.annotation.Bean;

//@Configuration
public class TthawkDynamicAspectPointcutAdvisorConfig {

    /**
     * 创建切面
     * @return
     */
    @Bean
    public TthawkDynamicAspectPointcutAdvisor manualbeanThirdPointcutAdvisor(){

        return new TthawkDynamicAspectPointcutAdvisor();
    }

}
