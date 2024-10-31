package cn.com.kun.springframework.core.ioc.manualbean;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManualbeanTimeLog {

    String name() default "";
}