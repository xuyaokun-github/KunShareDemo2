package cn.com.kun.component.tthawk.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class NestedExceptionHelper {

    public static Throwable buildNestedException(String exceptionList){

        Throwable targetThrowable = null;
        if (exceptionList != null){
            String[] exceptionArr = exceptionList.split(",");
            /*
                exceptionList: 1,2,3
                1的cause是2,2的cause是3,3的cause为null
             */
            Map<String, Throwable> throwableMap = new HashMap<>();
            int index = exceptionArr.length - 1;
            while (index >= 0){
                String currentExpName = exceptionArr[index];
                Throwable current = null;
                try {
                    current = buildException(currentExpName);
                    if (current != null){
                        Throwable theLatterExp = throwableMap.get(String.valueOf(index + 1));
                        current.initCause(theLatterExp);
                    }
                } catch (InstantiationException e) {
                    current = buildOneThrowableParamException(currentExpName, throwableMap.get(String.valueOf(index + 1)));
                    if (current == null){
                        //继续尝试构造
                        current = buildOneStringAndOneThrowableParamException(currentExpName, throwableMap.get(String.valueOf(index + 1)));
                    }
                }
                throwableMap.put(String.valueOf(index), current);
                index--;
            }
            targetThrowable = throwableMap.get("0");
        }
        return targetThrowable;
    }

    private static Throwable buildOneStringAndOneThrowableParamException(String className, Throwable cause) {

        Class clazz = null;
        Object sourceBean = null;
        try {
            clazz = Class.forName(className);
            Constructor<?> constructor = null;
            //这里做一个兼容处理
            if (className.equals("org.springframework.web.client.ResourceAccessException")){
                constructor = clazz.getConstructor(String.class, IOException.class);
            }else {
                constructor = clazz.getConstructor(String.class, Throwable.class);
            }
            sourceBean = constructor.newInstance(new Object[]{"", cause});
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (Throwable) sourceBean;
    }

    public static Throwable buildException(String exceptionClass) throws InstantiationException {
        Class clazz = null;
        Object throwable = null;
        try {
            clazz = Class.forName(exceptionClass);
            throwable = clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            //有些异常类没有无参构造函数
            if (e.getCause() instanceof java.lang.NoSuchMethodException){
                throw e;
            }else {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (Throwable) throwable;
    }

    private static Throwable buildOneThrowableParamException(String className, Throwable cause) {

        Class clazz = null;
        Object sourceBean = null;
        try {
            clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(Throwable.class);
            sourceBean = constructor.newInstance(new Object[]{cause});
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (Throwable) sourceBean;
    }

}
