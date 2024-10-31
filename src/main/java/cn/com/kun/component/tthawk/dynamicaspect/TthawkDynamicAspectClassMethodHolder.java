package cn.com.kun.component.tthawk.dynamicaspect;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/10/31
 * desc:
*/
public class TthawkDynamicAspectClassMethodHolder {

    private static Map<String, Set<String>> classMethodSetMap = new ConcurrentHashMap<>();

    private static Set<String> classSet = new HashSet<>();

    public static Set<String> getMethodSet(String classname) {

        return classMethodSetMap.get(classname);
    }


    public static boolean containsClass(String className) {

        return classSet.contains(className);
    }


    public static void add(String classname, String methodName) {

        Set<String> set = classMethodSetMap.get(classname);
        if (set == null){
            set = new HashSet<>();
            classMethodSetMap.put(classname, set);
        }
        set.add(methodName);
        classSet.add(classname);
    }

}
