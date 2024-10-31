package cn.com.kun.springframework.core.ioc.manualbean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManualbeanThirdPointcutAdvisorConfig {

    /**
     * 创建切面
     * @return
     */
    @Bean
    public ManualbeanThirdPointcutAdvisor manualbeanThirdPointcutAdvisor(){

        return new ManualbeanThirdPointcutAdvisor();
    }


}
