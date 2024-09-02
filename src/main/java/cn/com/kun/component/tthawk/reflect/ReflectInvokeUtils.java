package cn.com.kun.component.tthawk.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
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
                //便捷模式创建参数
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
                    //已经是字符串类型了，无需再反序列化
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
            if (PrimitiveClassUtils.isPrimitive(methodParamClassName)){
                clazz = PrimitiveClassUtils.getClassByPrimitive(methodParamClassName);
            }else {
                clazz = Class.forName(methodParamClassName);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }


    /**
     *
     * @param className
     * @return
     */
    private static Object buildParamObject(String className) {
        Class clazz = null;
        Object sourceBean = null;
        try {
            clazz = Class.forName(className);
            if (clazz.isEnum()){
                LOGGER.info("参数class类型是枚举类");
                sourceBean = buildParamObjectForEnum(clazz);
            }else {
                Constructor<?>[] constructors = clazz.getConstructors();
                boolean existsVoidParamConstructor = existsVoidParamConstructor(constructors);
                if (existsVoidParamConstructor){
                    //有些类假如没有空的构造函数，将会出问题(例如java.lang.Integer)
                    sourceBean = clazz.newInstance();
                }else {
                    //没有无参构造函数
                    //递归创建
                    LOGGER.info("没有无参构造函数");
                    sourceBean = buildParamObjectForFixedParamConstructor(clazz, constructors[0]);
                }

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


    private static Object buildParamObjectForFixedParamConstructor(Class clazz, Constructor<?> constructor) {

        Class<?>[] constructorParameterTypesClasses = constructor.getParameterTypes();
        Object[] args = new Object[constructorParameterTypesClasses.length];
        Object sourceBean = null;
        try {
            for (int i = 0; i < constructorParameterTypesClasses.length; i++) {
                Class constructorParameterTypesClass = constructorParameterTypesClasses[i];
                args[i] = buildParamObject(constructorParameterTypesClass.getName());
            }
            sourceBean = constructor.newInstance(args);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return sourceBean;
    }

    private static boolean existsVoidParamConstructor(Constructor<?>[] constructors) {

        for (Constructor constructor : constructors){

            Class<?>[] classes = constructor.getParameterTypes();
            if (classes.length == 0){
                return true;
            }
        }

        return false;
    }

    private static Object buildParamObjectForEnum(Class clazz) {

        Method valuesMethod = null;
        try {
            valuesMethod = clazz.getMethod("values");
            Object allEnumObj = valuesMethod.invoke(null);
            System.out.println(allEnumObj);
            if (allEnumObj instanceof Object[]){
                Object[] objectsj = (Object[]) allEnumObj;
                //拿到第一个元素
                return objectsj[0];
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}
