package cn.com.kun.component.tthawk.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectInvokeUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReflectInvokeUtils.class);

    public static void doAttack(ReflectVO reflectVO) {

        String methodName = reflectVO.getMethod();
        try {
            Class[] methodParams = new Class[reflectVO.getMethodParamSize()];
            for (int i = 0; i < reflectVO.getMethodParamSize(); i++) {
                String index = String.valueOf(i + 1);
                methodParams[i] = getMethodClass(reflectVO.getMethodClassMap().get(index));
            }

            Object[] args = getExecArgs(reflectVO);
            Object sourceBean = getSourceBean(reflectVO);
            if (sourceBean == null){
                LOGGER.info("sourceBean为空，停止执行");
                return;
            }
            Method method = sourceBean.getClass().getDeclaredMethod(methodName, methodParams);
            method.setAccessible(true);
            method.invoke(sourceBean, args);
        }  catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static Object[] getExecArgs(ReflectVO reflectVO) {

        Object[] args = new Object[reflectVO.getMethodParamSize()];
        for (int i = 0; i < reflectVO.getMethodParamSize(); i++) {
            String index = String.valueOf(i + 1);
            if (reflectVO.getParamClassMap() != null && reflectVO.getParamClassMap().containsKey(index)){
                String paramClass = reflectVO.getParamClassMap().get(index);
                if (needNestedException(index, reflectVO.getNestedExceptionClassMap())){
                    args[i] = NestedExceptionHelper.buildNestedException(reflectVO.getNestedExceptionClassMap().get(index));
                } else {
                    args[i] = buildParamObject(paramClass);
                }
            } else if (reflectVO.getJsonParamClassMap() != null && reflectVO.getJsonParamValueMap() != null
                    && reflectVO.getJsonParamClassMap().containsKey(index)
                    && reflectVO.getJsonParamValueMap().containsKey(index)){
                String className = reflectVO.getJsonParamClassMap().get(index);
                String json = reflectVO.getJsonParamValueMap().get(index);
                if (className.equals(String.class.getName())){
                    args[i] = TthawkBase64Utils.decrypt(json);
                }else {
                    args[i] = toJavaObj(reflectVO, json, className);
                }
            } else {
                args[i] = null;
            }
        }

        return args;
    }

    private static Object toJavaObj(ReflectVO reflectVO, String jsonSource, String paramClassName) {

        Object result = null;
        try {
            String decryptSourceJson = TthawkBase64Utils.decrypt(jsonSource);
            LOGGER.info("解密后的json源报文：{}", decryptSourceJson);
            Class clazz = Class.forName(reflectVO.getJsonUtilsClassName());
            Class paramClass = Class.forName(paramClassName);
            Object sourceBean = clazz.newInstance();
            Class[] methodParams = new Class[2];
            methodParams[0] = String.class;
            methodParams[1] = Class.class;
            Method method = sourceBean.getClass().getDeclaredMethod("toJavaObject", methodParams);
            method.setAccessible(true);
            Object[] args = new Object[2];
            args[0] = decryptSourceJson;
            args[1] = paramClass;
            result = method.invoke(sourceBean, args);
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

        return result;
    }

    private static Object getSourceBean(ReflectVO reflectVO) {

        Object sourceBean = null;
        Class clazz = null;
        try {

            if (AcquireBeanMode.SPRING.getMode().equals(reflectVO.getAcquireBeanMode())){
                sourceBean = reflectVO.getSpringBean();
            }else {
                clazz = Class.forName(reflectVO.getClassName());
                sourceBean = clazz.newInstance();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return sourceBean;
    }

    private static boolean needNestedException(String index, Map<String, String> nestedExceptionClassMap) {

        return nestedExceptionClassMap != null && nestedExceptionClassMap.containsKey(index);
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
