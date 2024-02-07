package cn.com.kun.springframework.actuator.endpoint;

import cn.com.kun.common.utils.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Endpoint springboot2.7.12写法
 *
 * author:xuyaokun_kzx
 * date:2024/2/7
 * desc:
*/
@Component
@Endpoint(id = "cacheClear", enableByDefault = true) //设置 id，并选择是否默认开启
public class CustomCacheEndPoint {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomCacheEndPoint.class);

    @Autowired
    @Qualifier("caffeineCacheManager")
    private CacheManager cacheManager;


    /**
     * 正确的访问方法：
     * http://localhost:10002/kunsharedemo27/actuator/cacheClear
     *
     * @return
     */
    @ReadOperation
    public String refresh() {
        invoke();
        return "My CustomCacheEndPoint refresh";
    }

    /**
     * 假如定义多个无参的方法会报错：
     * Caused by: java.lang.IllegalStateException: Unable to map duplicate endpoint operations:
     * [web request predicate GET to path 'cacheClear' produces: application/vnd.spring-boot.actuator.v3+json,application/vnd.spring-boot.actuator.v2+json,application/json] to customCacheEndPoint
     *
     * @return
     */
//    @ReadOperation
//    public String refresh2() {
//        invoke();
//        return "My CustomCacheEndPoint refresh";
//    }

    /**
     * 正确的访问方式：http://localhost:10002/kunsharedemo27/actuator/cacheClear/kkk
     * 这里的kkk就是方法的入参name
     *
     * @param name
     * @return
     */
    @ReadOperation
    public String refresh3(@Selector String name) {
        invoke();
        return "My CustomCacheEndPoint refresh 3";
    }

    /**
     * 我们需要通过重写 invoke 方法，返回我们要监控的内容。
     *
     * @return
     */
    private Map<String, Object> invoke() {

        Collection<String> collection =  cacheManager.getCacheNames();
        try {
            collection.forEach(cacheName ->{
                cacheManager.getCache(cacheName).clear();
            });
        }catch (Exception e){
            LOGGER.error("CustomCacheEndPoint error", e);
        }

        Map<String, Object> result = new HashMap<>();
        Date date = new Date();
        result.put("server_time", date.toString());
        result.put("msg", "清缓存成功,缓存器列表：" + JacksonUtils.toJSONString(collection));
        return result;
    }

}
