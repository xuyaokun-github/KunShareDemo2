package cn.com.kun.component.validationx.common.annotationvalidator;


import cn.com.kun.component.validationx.annotation.NotNullExtendField;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationValidatorFactory {

    private static Map<Class, AnnotationValidator> annotationValidatorMap = new ConcurrentHashMap<>();

    static {
        annotationValidatorMap.put(NotNullExtendField.class, new NotNullExtendAnnotationValidator());
    }


    public static boolean exists(Annotation annotation) {

        //注意这里不能用annotation.getClass()，可能拿到的是代理类，应该用annotation.annotationType()
        return annotationValidatorMap.containsKey(annotation.annotationType());
    }

    public static AnnotationValidator get(Annotation annotation) {

        return annotationValidatorMap.get(annotation.annotationType());
    }

}
