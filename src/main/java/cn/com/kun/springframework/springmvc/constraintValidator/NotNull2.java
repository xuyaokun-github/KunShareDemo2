package cn.com.kun.springframework.springmvc.constraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 放到类上，做多字段联合校验
 *
 * author:xuyaokun_kzx
 * date:2024/9/4
 * desc:
*/
@Target({ElementType.TYPE, METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {NotNull2Validator.class})
public @interface NotNull2 {

    // 默认错误消息
    String message() default "Student参数有误";

    // 分组
    Class<?>[] groups() default {};

    // 负载
    Class<? extends Payload>[] payload() default {};

}
