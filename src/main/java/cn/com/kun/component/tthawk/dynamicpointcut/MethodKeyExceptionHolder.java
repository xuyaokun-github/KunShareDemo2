package cn.com.kun.component.tthawk.dynamicpointcut;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodKeyExceptionHolder {

    private static Map<String, String> exceptionClassMap = new ConcurrentHashMap<>();

    public static String getExceptionClass(String methodKey) {

        return exceptionClassMap.get(methodKey);
    }

    public static boolean contains(String methodKey) {

        return exceptionClassMap.containsKey(methodKey);
    }


    public static void add(String methodKey, String exceptionClass) {

        exceptionClassMap.put(methodKey, exceptionClass);
    }


    public static void remove(String methodKey) {

        exceptionClassMap.remove(methodKey);
    }


}
