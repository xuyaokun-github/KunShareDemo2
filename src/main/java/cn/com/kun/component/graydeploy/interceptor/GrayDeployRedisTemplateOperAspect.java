package cn.com.kun.component.graydeploy.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Aspect
@Component
public class GrayDeployRedisTemplateOperAspect {

    private final static String GRAY_REDIS_KEY_SUFFIX = "_gray";

    @Pointcut("execution(public * org.springframework.data.redis.core.RedisTemplate.delete(..))")
    private void delete() {

    }

    /**
     * 删除key切面
     */
    @Around("delete()")
    public Object delete(ProceedingJoinPoint pjp) throws Throwable {

        Object[] args = pjp.getArgs();

        Object key = (String) args[0];
        if (key instanceof String) {
            String stringKey = (String) key;
            if (stringKey != null && stringKey.length() > 0){
                String newKey = stringKey + GRAY_REDIS_KEY_SUFFIX;
                args[0] = newKey;
            }
        } else if (key instanceof Collection) {
            //actions
            Collection<String> collection = (Collection) key;
            List<String> list = new ArrayList<>();
            for (String str : collection){
                if (str != null && str.length() > 0){
                    String newKey = str + GRAY_REDIS_KEY_SUFFIX;
                    list.add(newKey);
                }
            }
            args[0] = list;
        }

        Object result = pjp.proceed(args);
        return result;
    }


}
