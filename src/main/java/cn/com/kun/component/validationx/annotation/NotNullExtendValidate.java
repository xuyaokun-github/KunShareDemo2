package cn.com.kun.component.validationx.annotation;

import cn.com.kun.component.validationx.validator.NotNullExtendValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 放在类上
 *
 * author:xuyaokun_kzx
 * date:2024/9/4
 * desc:
*/
@Target({ElementType.TYPE, METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {NotNullExtendValidator.class})
public @interface NotNullExtendValidate {

    // 默认错误消息
    String message() default "%s参数不能为空";

    // 分组
    Class<?>[] groups() default {};

    // 负载
    Class<? extends Payload>[] payload() default {};

}
