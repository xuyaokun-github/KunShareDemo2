package cn.com.kun.component.tthawk.dynamicpointcut;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/8/1
 * desc:
*/
public class MethodKeyExceptionHolder {

    private static Map<String, String> exceptionClassMap = new ConcurrentHashMap<>();

    private static Map<String, String> nestedExceptionClassMap = new ConcurrentHashMap<>();

    public static String getExceptionClass(String methodKey) {

        return exceptionClassMap.get(methodKey);
    }

    public static String getNestedExceptionClass(String methodKey) {

        return nestedExceptionClassMap.get(methodKey);
    }

    public static boolean contains(String methodKey) {

        return exceptionClassMap.containsKey(methodKey);
    }


    public static void add(String methodKey, String exceptionClass) {

        exceptionClassMap.put(methodKey, exceptionClass);
    }


    public static void remove(String methodKey) {

        exceptionClassMap.remove(methodKey);
        nestedExceptionClassMap.remove(methodKey);
    }


    public static void addNestedException(String methodKey, String nestedExceptionClass) {

        nestedExceptionClassMap.put(methodKey, nestedExceptionClass);
    }

}
