package cn.com.kun.component.tthawk.reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrimitiveClassUtils {

    private static Map<String, Class> primitiveClassMap = new ConcurrentHashMap<>();

    static {
        primitiveClassMap.put("int", int.class);
        primitiveClassMap.put("double", double.class);
        primitiveClassMap.put("char", char.class);
        primitiveClassMap.put("byte", byte.class);
        primitiveClassMap.put("short", short.class);
        primitiveClassMap.put("float", float.class);
        primitiveClassMap.put("long", long.class);
        primitiveClassMap.put("boolean", boolean.class);
    }

    public static boolean isPrimitive(String methodParamClassName) {

        return primitiveClassMap.containsKey(methodParamClassName);
    }

    public static Class getClassByPrimitive(String methodParamClassName) {

        return primitiveClassMap.get(methodParamClassName);
    }
}
