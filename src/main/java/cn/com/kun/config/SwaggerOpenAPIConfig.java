package cn.com.kun.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 新版swagger
 *
 * author:xuyaokun_kzx
 * date:2023/9/26
 * desc:
*/
@Configuration
public class SwaggerOpenAPIConfig{

    //创建Docket对象
    @Bean
    public OpenAPI springShopOpenApi(){
        Info info = new Info()
                .title("KunShareDemo Springboot2.7.12版本")
                .version("v1.0")
                .description("Springboot2.7.12版本")
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://kunghsu.com")
                );

        return new OpenAPI().info(info).externalDocs(new ExternalDocumentation()
                .description("doc").url("http://kunghsu.com"));
    }

}
