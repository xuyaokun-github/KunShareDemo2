package cn.com.kun.component.validationx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullExtendField {

    String condition() default "";

    String columnName() default "";

    /**
     * 是否允许为空串
     * 假如为true，则参数为空串，放行
     * @return
     */
    boolean allowEmpty() default false;

    /**
     * 是否允许为空格
     * @return
     */
    boolean allowBlank() default false;

}
