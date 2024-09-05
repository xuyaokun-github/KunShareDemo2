package cn.com.kun.component.validationx.validator;

import cn.com.kun.component.validationx.annotation.NotNullExtendField;
import cn.com.kun.component.validationx.annotation.NotNullExtendValidate;
import cn.com.kun.component.validationx.support.SpELTools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 多字段联动校验
 *
 * author:xuyaokun_kzx
 * date:2024/9/2
 * desc:
*/
public class NotNullExtendValidator implements ConstraintValidator<NotNullExtendValidate, Object> {

    private final static Logger LOGGER = LoggerFactory.getLogger(NotNullExtendValidator.class);

    private String message;

    @Override
    public void initialize(NotNullExtendValidate notNullExtendValidate) {
        this.message = notNullExtendValidate.message();
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        //反射拿到所有字段
        try {
            Class cls = value.getClass();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields){
                NotNullExtendField notNullExtendField = field.getAnnotation(NotNullExtendField.class);
                if (notNullExtendField != null){
                    //进行校验
                    String conditon = notNullExtendField.condition();
                    boolean needCheck = SpELTools.parseExpressionToBoolean(value, conditon);
                    if (needCheck){
                        //先获取属性旧值
                        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
                        Method getMethod = pd.getReadMethod();
                        //拿到属性旧值
                        Object oldValue = getMethod.invoke(value);
                        if (oldValue == null){
                            LOGGER.info("条件[{}]成立时，字段[{}]不能为null", conditon, field.getName());
                            buildErrorMessage(context, notNullExtendField.columnName());
                            return false;
                        }
                        if (oldValue instanceof String){
                            String string = (String) oldValue;
                            if (!notNullExtendField.allowEmpty() && StringUtils.isEmpty(string)){
                                LOGGER.info("条件[{}]成立时，字段[{}]不能为空串", conditon, field.getName());
                                buildErrorMessage(context, notNullExtendField.columnName());
                                return false;
                            }
                            if (!notNullExtendField.allowBlank() && StringUtils.isBlank(string)){
                                LOGGER.info("条件[{}]成立时，字段[{}]不能为空格", conditon, field.getName());
                                buildErrorMessage(context, notNullExtendField.columnName());
                                return false;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            LOGGER.warn("NotNullExtendValidator校验异常", e);
        }

        return true;
    }

    private void buildErrorMessage(ConstraintValidatorContext context, String columnName) {

        //禁用默认提示信息
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(String.format(message, columnName)).addConstraintViolation();

    }

}
