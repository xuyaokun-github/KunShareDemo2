package cn.com.kun.springframework.core.ioc.beanoverride;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class CharacterEncodingFilterConfig2 {

    @Bean
    CharacterEncodingFilter myCharacterEncodingFilter(){
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("222");
        return characterEncodingFilter;
    }


}
