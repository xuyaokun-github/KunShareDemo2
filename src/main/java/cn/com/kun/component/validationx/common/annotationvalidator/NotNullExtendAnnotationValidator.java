package cn.com.kun.component.validationx.common.annotationvalidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotNullExtendAnnotationValidator implements AnnotationValidator {

    private final static Logger LOGGER = LoggerFactory.getLogger(NotNullExtendAnnotationValidator.class);

    @Override
    public boolean validate(Object value) {

        LOGGER.info("开始NotNullExtend验证");
        //TODO

        return true;
    }


}
