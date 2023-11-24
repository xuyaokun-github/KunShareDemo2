package cn.com.kun.component.twiceaccess.aspect;

import cn.com.kun.component.twiceaccess.annotation.TwiceAccess;
import cn.com.kun.component.twiceaccess.core.TwiceAccessRedisAccessor;
import cn.com.kun.component.twiceaccess.executor.TwiceAccessExecutor;
import cn.com.kun.component.twiceaccess.vo.TwiceAccessReturnVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static cn.com.kun.component.twiceaccess.constant.TwiceAccessConstants.*;

/**
 * 二次访问组件切面
 *
 * author:xuyaokun_kzx
 * date:2023/11/21
 * desc:
*/
@Component
@Aspect
public class TwiceAccessAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(TwiceAccessAspect.class);

    @Pointcut("@annotation(cn.com.kun.component.twiceaccess.annotation.TwiceAccess)")
    public void pointCut(){


    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        HttpServletRequest request = null;
        HttpServletResponse response = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes){
            request = ((ServletRequestAttributes)requestAttributes).getRequest();
            response = ((ServletRequestAttributes)requestAttributes).getResponse();
        }
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        String className = pjp.getTarget().getClass().getSimpleName();
        String fullMethodName = className + "." + methodName;

        // 获取方法上的TwiceAccess注解对象
        TwiceAccess twiceAccess = method.getAnnotation(TwiceAccess.class);
        long waitTime = twiceAccess.waitTime();

        Object result = null;

        //判断为需要进行二次访问
        if (isFirstAccess(request) && isRedisAccessorEnabled()){
            LOGGER.info("二次访问-阶段1，开始转异步");
            String requestId = UUID.randomUUID().toString();
            //异步
            TwiceAccessExecutor.execAsync(()->{
                try {
                    Object realResult = pjp.proceed();
                    //放Redis
                    storeResult(requestId, realResult, waitTime, fullMethodName);
                } catch (Throwable e) {
                    LOGGER.warn("业务层执行失败", e);
                    //放置一个失败的结果
                    storeFailResult(requestId, waitTime, e);
                }

            });
            //返回一个固定的结果
            //直接返回另一个类型的VO,会有类型转换问题
//            return twiceAccessReturnVO;
            //假如全部工程统一了返回VO,例如下面的ResultVo，也可以返回ResultVo，但可能不是每个控制层方法返回的都是统一的VO,所以在这里定死ResultVo不太好
//            return ResultVo.valueOfSuccess(twiceAccessReturnVO);
            //设置response headers，返回空
            setResponseHeaders(response, TwiceAccessReturnVO.firstRtnVO(waitTime, requestId));
            return null;
        } if (isSecondAccess(request) && isRedisAccessorEnabled() && request != null) {
            String requestId = request.getParameter(TWICE_ACCESS_REQUEST_ID);
            if (requestId == null || requestId.length() == 0){
                requestId = request.getHeader(TWICE_ACCESS_HEADER_QUERY_REQUEST_ID);
            }
            LOGGER.info("二次访问-阶段2，获取结果requestId：{}", requestId);
            //从Redis中取出结果返回
            result = getResult(requestId);
            if (result == null){
                //假如从Redis中取不到，说明后台仍未处理完，则返回
//                return twiceAccessReturnVO;
//                return ResultVo.valueOfSuccess(twiceAccessReturnVO);
                setResponseHeaders(response, TwiceAccessReturnVO.resultNotReadyVO(waitTime, requestId));
                return null;
            } if (result instanceof TwiceAccessReturnVO && ((TwiceAccessReturnVO)result).failResult()) {
                setResponseHeaders(response, ((TwiceAccessReturnVO)result));
                return null;
            }else {
                //建议不要返回中文，可能会导致乱码问题
                setResponseHeaders(response, TwiceAccessReturnVO.resultResultAlreadyReturnedVO(waitTime, requestId));
            }
        }else {
            //前端不要求二次访问，直接走原逻辑
            result = pjp.proceed();
        }

        return result;
    }

    private void storeFailResult(String requestId, long waitTime, Throwable e) {

        if (TwiceAccessRedisAccessor.getRedisAccessor().getRedisTemplate() == null){
            LOGGER.warn("TwiceAccess强依赖Redis，请补充redis实现，二次访问组件功能失效");
            return ;
        }
        //过期时间，默认在原来的基础上加 5分钟
        long expiredTime = waitTime + 5 * 60 * 1000;
        String redisKey = buildKey(requestId);
        String failMessage = "";
        try {
            failMessage = URLEncoder.encode(e.getMessage(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOGGER.warn("TwiceAccess错误描述转码出现不支持编码", ex);
        }
        TwiceAccessReturnVO twiceAccessReturnVO = TwiceAccessReturnVO.resultFailVO(failMessage);
        TwiceAccessRedisAccessor.getRedisAccessor().getRedisTemplate().opsForValue().set(redisKey, twiceAccessReturnVO, expiredTime, TimeUnit.MILLISECONDS);
    }

    private boolean isRedisAccessorEnabled() {

        return TwiceAccessRedisAccessor.getRedisAccessor().getRedisTemplate() != null;
    }

    private void setResponseHeaders(HttpServletResponse response, TwiceAccessReturnVO twiceAccessReturnVO) {

        if (response != null){
            response.addHeader(TWICE_ACCESS_HEADER_RTN_TYPE, twiceAccessReturnVO.getRtnType());
            response.addHeader(TWICE_ACCESS_HEADER_WAITTIME, String.valueOf(twiceAccessReturnVO.getWaitTime()));
            response.addHeader(TWICE_ACCESS_HEADER_REQUEST_ID, twiceAccessReturnVO.getRequestId());
            response.addHeader(TWICE_ACCESS_HEADER_MESSAGE, twiceAccessReturnVO.getMessage());
        }
    }

    private Object getResult(String requestId) {

        if (TwiceAccessRedisAccessor.getRedisAccessor().getRedisTemplate() == null){
            LOGGER.warn("TwiceAccess强依赖Redis，请补充redis实现，二次访问组件功能失效");
            return null;
        }
        String redisKey = buildKey(requestId);
        return TwiceAccessRedisAccessor.getRedisAccessor().getRedisTemplate().opsForValue().get(redisKey);
    }

    private String buildKey(String requestId) {

        return TWICE_ACCESS_REDIS_KEY_PREFIX + ":" + requestId;
    }

    private void storeResult(String requestId, Object realResult, long waitTime, String fullMethodName) {

        if (TwiceAccessRedisAccessor.getRedisAccessor().getRedisTemplate() == null){
            LOGGER.warn("TwiceAccess强依赖Redis，请补充redis实现，二次访问组件功能失效");
            return ;
        }
        //过期时间，默认在原来的基础上加 5分钟
        long expiredTime = waitTime + 5 * 60 * 1000;
        String redisKey = buildKey(requestId);
        TwiceAccessRedisAccessor.getRedisAccessor().getRedisTemplate().opsForValue().set(redisKey, realResult, expiredTime, TimeUnit.MILLISECONDS);
    }


    private boolean isFirstAccess(HttpServletRequest request) {

        String twiceAccessQueryType = getTwiceAccessQueryType(request);
        return TWICE_ACCESS_QUERY_TYPE_OF_FIRST.equals(twiceAccessQueryType);
    }

    private boolean isSecondAccess(HttpServletRequest request) {

        String twiceAccessQueryType = getTwiceAccessQueryType(request);
        return TWICE_ACCESS_QUERY_TYPE_OF_SECOND.equals(twiceAccessQueryType);
    }

    private String getTwiceAccessQueryType(HttpServletRequest request) {

        String twiceAccessQueryType = "";
        if (request != null){
            twiceAccessQueryType = request.getParameter(TWICE_ACCESS_QUERY_TYPE);
            if (twiceAccessQueryType == null){
                twiceAccessQueryType = request.getHeader(TWICE_ACCESS_HEADER_QUERY_TYPE);
            }
        }
        return twiceAccessQueryType;
    }


}
