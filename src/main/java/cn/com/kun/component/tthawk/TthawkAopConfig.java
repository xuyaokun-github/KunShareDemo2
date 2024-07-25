package cn.com.kun.component.tthawk;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Configuration
//@EnableAspectJAutoProxy
public class TthawkAopConfig {


}