package cn.com.kun.component.validationx.common;

import cn.com.kun.component.validationx.common.annotationvalidator.AnnotationValidator;
import cn.com.kun.component.validationx.common.annotationvalidator.AnnotationValidatorFactory;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * 多字段联动校验
 * （暂不支持分组，因为没法在本类中获取controller方法上指定的分组,所以这里很难再扩展下去了，不太方便继续扩展了）
 *  假如不需要分组的可以继续用这个组件。
 *
 * author:xuyaokun_kzx
 * date:2024/9/2
 * desc:
*/
public class CommonExtendValidator implements ConstraintValidator<CommonExtendValidate, Object> {


    private Class<?>[] groups;

    @Override
    public void initialize(CommonExtendValidate constraintAnnotation) {

        //这里拿到的是实体类上引入注解时所指定的分组，并不是controller方法上指定的分组
        groups = constraintAnnotation.groups();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // 不为null才进行校验
        if (value == null) {
            return false;
        }

        //在这里拿到的也是 实体类上引入注解时所指定的分组
        Set<Class<?>> groups = ((ConstraintValidatorContextImpl)context).getConstraintDescriptor().getGroups();

        /*
            如何拿到controller方法上指定的分组呢？
            框架没有考虑这个
         */

        //反射拿到所有字段
        Class cls = value.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields){
            //通过注解找实现
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations){
                boolean exist = AnnotationValidatorFactory.exists(annotation);
                if (exist){
                    AnnotationValidator annotationValidator = AnnotationValidatorFactory.get(annotation);
                    boolean validateRes = annotationValidator.validate(value);
                    if (!validateRes){
                        //验证失败
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
