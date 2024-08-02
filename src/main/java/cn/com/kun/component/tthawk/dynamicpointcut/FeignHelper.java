package cn.com.kun.component.tthawk.dynamicpointcut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * 为11.10版feign设计
 *
 * author:xuyaokun_kzx
 * date:2024/8/2
 * desc:
*/
public class FeignHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(FeignHelper.class);


    /**
     * 判断是否为feign代理类
     *
     * @param target
     * @return
     */
    public static boolean isFeignProxy(Object target) {

        return target.getClass().getName().contains("com.sun.proxy.$Proxy") &&
                target.toString().contains("HardCodedTarget");
    }


    /**
     * 用两种方法获取feign client的全限定名
     *
     * @param target
     * @return
     */
    public static String getFeignClientClassName(Object target) {

        String classname = getFeignClientClassNameByReflect(target);
        if (classname != null){
            LOGGER.info("获取feign client classname");
        }
        return classname != null ? classname : target.toString().split(",")[0].replace("HardCodedTarget(type=", "");
    }


    public static String getFeignClientClassNameByReflect(Object proxy) {

        try {
            Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
            h.setAccessible(true);
            Object aopProxy =  h.get(proxy);

            Field arg$3 = aopProxy.getClass().getDeclaredField("arg$3");
            arg$3.setAccessible(true);

            Object innetType = arg$3.get(aopProxy);

            Field type = innetType.getClass().getDeclaredField("type");
            type.setAccessible(true);

            Object classType = type.get(innetType);
            return ((Class)classType).getName();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
