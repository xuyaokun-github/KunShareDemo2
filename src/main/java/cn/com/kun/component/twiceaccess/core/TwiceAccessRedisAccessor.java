package cn.com.kun.component.twiceaccess.core;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * TwiceAccess组件强依赖Redis
 *
 * author:xuyaokun_kzx
 * date:2023/11/21
 * desc:
*/
@Component
public class TwiceAccessRedisAccessor implements InitializingBean {

    private static TwiceAccessRedisAccessor redisAccessor = null;

    //单例模式
    public static TwiceAccessRedisAccessor getRedisAccessor() {
        return redisAccessor;
    }

    /**
     * 这种做法的缺点：不够通用，限定了使用方必须使用RedisTemplate，没有给使用方灵活选择Redis框架的权利
     */
    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    public RedisTemplate getRedisTemplate(){

        return redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisAccessor = this;
    }

}
