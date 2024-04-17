package cn.com.kun.springframework.springmvc.resttemplate;

import org.apache.http.NoHttpResponseException;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;

/**
 * 自定义的重试处理器（仅适用HttpClient框架）
 *
 * author:xuyaokun_kzx
 * date:2024/4/12
 * desc:
*/
public class CustomHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomHttpRequestRetryHandler.class);

    public CustomHttpRequestRetryHandler() {
        super();
    }

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {

        //假如是明确的可重试类异常，则直接返回true，表示可重试
        if (isRetryableException(exception)){
            LOGGER.info("准备进行重试，当前重试次数：{}", executionCount);
            return true;
        }

        return super.retryRequest(exception, executionCount, context);
    }

    private boolean isRetryableException(IOException e) {

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

    /**
     * 不建议复写仅handleAsIdempotent方法，没法针对异常进一步判断
     * @param request
     * @return
     */
//    @Override
//    protected boolean handleAsIdempotent(HttpRequest request) {
//
//        /*
//            父类默认逻辑：!(request instanceof HttpEntityEnclosingRequest)
//            请求不含body就重试，含body就不重试
//         */
//        //super.handleAsIdempotent(request)
//        return true;
//    }

}
