package cn.com.kun.component.tthawk.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectInvokeUtils {


    public static void doAttack(ReflectVO reflectVO) {

        String className = reflectVO.getClassName();
        String methodName = reflectVO.getMethod();
        Object[] args = new Object[reflectVO.getMethodParamSize()];
        Class[] methodParams = new Class[reflectVO.getMethodParamSize()];

        for (int i = 0; i < reflectVO.getMethodParamSize(); i++) {
            String index = String.valueOf(i + 1);
            if (reflectVO.getParamClassMap() != null && reflectVO.getParamClassMap().containsKey(index)){
                args[i] = buildParamObject(reflectVO.getParamClassMap().get(index));
            }else {
                args[i] = null;
            }

            methodParams[i] = getMethodClass(reflectVO.getMethodClassMap().get(index));
        }
        
        Class clazz = null;
        try {
            clazz = Class.forName(className);
            Object sourceBean = clazz.newInstance();
            Method method = sourceBean.getClass().getDeclaredMethod(methodName, methodParams);
            method.invoke(sourceBean, args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static Class getMethodClass(String methodParamClassName) {
        Class clazz = null;
        try {
            clazz = Class.forName(methodParamClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    private static Object buildParamObject(String className) {
        Class clazz = null;
        Object sourceBean = null;
        try {
            clazz = Class.forName(className);
            sourceBean = clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sourceBean;
    }

}
