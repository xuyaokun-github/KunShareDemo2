package cn.com.kun.springframework.springcloud.feign.Interceptor;

import feign.RetryableException;
import feign.Retryer;
import org.apache.http.NoHttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.net.SocketException;

/**
 * 针对NoHttpResponseException等异常的Feign重试处理器
 * 假如定义了Retryer类型的bean，会自动覆盖 feign.Retryer#NEVER_RETRY
 *
 * author:xuyaokun_kzx
 * date:2023/9/13
 * desc:
*/
@ConditionalOnProperty(prefix = "feign.exception-retryer", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Component
public class FeignExceptionRetryer implements Retryer {

    private final static Logger LOGGER = LoggerFactory.getLogger(FeignExceptionRetryer.class);

    /**
     * 默认重试三次
     */
    private int retryCount = 3;

    public FeignExceptionRetryer() {

    }

    @Override
    public void continueOrPropagate(RetryableException e) {

        LOGGER.info("进入FeignExceptionRetryer");
        if (e != null){
            Throwable throwable = e.getCause();
            if (isRetryableException(throwable)){
                if (retryCount > 0){
                    //直接return 即表示可以重试
                    retryCount--;
                    LOGGER.info("准备进行重试，重试次数剩余次数：{}", retryCount);
                    return;
                }else {
                    LOGGER.info("Feign重试次数已到上限，执行终止");
                    throw new RuntimeException("Feign重试次数已到上限，执行终止");
                }
            }else {
                //抛出异常，即终止整个流程
                throw e;
            }
        }

    }

    /**
     * 是否可重试类的异常
     *
     * @param e
     * @return
     */
    private boolean isRetryableException(Throwable e) {

        if (e instanceof NoHttpResponseException){
            LOGGER.info("出现NoHttpResponseException");
            return true;
        }

        //java.net.SocketException: Software caused connection abort: recv failed
        if (e instanceof SocketException && e.getMessage().contains("Software caused connection abort: recv failed")){
            LOGGER.info("出现SocketException异常-Software caused connection abort: recv failed");
            return true;
        }

        return false;
    }

    @Override
    public Retryer clone() {
        return new FeignExceptionRetryer();
    }
}
