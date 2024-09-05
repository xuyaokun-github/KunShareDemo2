package cn.com.kun.springframework.springmvc.constraintValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 多字段联动校验
 *
 * author:xuyaokun_kzx
 * date:2024/9/2
 * desc:
*/
public class NotNull2Validator implements ConstraintValidator<NotNull2, Object> {

    @Override
    public void initialize(NotNull2 constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // 不为null才进行校验        
        if (value == null) {
            return false;
        }

        return true;
    }


}
