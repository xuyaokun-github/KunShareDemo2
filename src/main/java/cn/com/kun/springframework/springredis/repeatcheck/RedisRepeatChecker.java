package cn.com.kun.springframework.springredis.repeatcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis防重工具类V1.0
 *
 * Created by xuyaokun On 2022/11/12 1:13
 * @desc:
 *
 */
@Component
public class RedisRepeatChecker {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisRepeatChecker.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 假如重复，返回false
     * 假如未重复，返回true 说明校验通过
     *
     * 防重次数默认为1次
     *
     * @return
     */
    public boolean checkAndSet(String key){

        //判断key值是否存在，存在则不存储，不存在则存储
        return checkAndSet(key, 1, TimeUnit.HOURS);
    }

    /**
     *
     * @param key
     * @param timeout 自定义过期时间
     * @param unit 自定义过期时间（时间单位）
     * @return
     */
    public boolean checkAndSet(String key, long timeout, TimeUnit unit){

        return redisTemplate.opsForValue().setIfAbsent(key, key, timeout, unit);
    }


    /**
     * 假如重复，返回false
     * 假如未重复，返回true 说明校验通过
     *
     * 防重次数为N
     *
     * @return
     */
    public boolean check(String key, int repeatTimes){

        //判断key值是否存在，存在则不存储，不存在则存储
        return check(key, repeatTimes, 1, TimeUnit.HOURS);
    }

    /**
     * 假如重复，返回false
     * 假如未重复，返回true 说明校验通过
     *
     * @param key
     * @param repeatTimes 未超过重复次数都算不重复
     * @param timeout 自定义过期时间
     * @param unit 自定义过期时间（时间单位）
     * @return
     */
    public boolean check(String key, int repeatTimes, int timeout, TimeUnit unit) {

        boolean checkFlag = true;
        try {
            //尝试加1，拿到返回值
            long res = redisTemplate.opsForValue().increment(key);
            if (res == 1){
                //假如是初次，设置过期时间
                redisTemplate.expire(key, timeout, unit);
            }
            //true: 表示未重复（未超过重复次数都算不重复）
            checkFlag = res <= repeatTimes ? true : false;

        }catch (Exception e){
            //假如出异常，默认返回true，表示不拦截
            LOGGER.error("Redis防重校验异常,key:{}", key, e);
        }

        return checkFlag;
    }

}
