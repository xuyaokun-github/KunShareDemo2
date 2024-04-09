package cn.com.kun.common.advice;

import cn.com.kun.common.exception.BizException;
import cn.com.kun.common.utils.WebUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.ratelimiter.exception.RateLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一的异常处理器
 * author:xuyaokun_kzx
 * date:2021/5/14
 * desc:
*/
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResultVo bizExceptionHandler(HttpServletRequest req, BizException e){
        LOGGER.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return ResultVo.error(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = RateLimitException.class)
    @ResponseBody
    public ResultVo rateLimitExceptionHandler(HttpServletRequest req, RateLimitException e){
        LOGGER.error("发生限流异常！原因是：{}", e.getErrorMsg());
        return ResultVo.error(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public ResultVo exceptionHandler(HttpServletRequest req, NullPointerException e){
        LOGGER.error("发生空指针异常！原因是:",e);
        return ResultVo.error(CommonEnum.INTERNAL_SERVER_ERROR);
    }


    /**
     * 处理其他异常
     *
     * 方法1
     *
     * @param req
     * @param e
     * @return
     */
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
    public ResultVo exceptionHandler(HttpServletRequest req, Exception e){
        LOGGER.error("未知异常！异常堆栈:", e);
        return ResultVo.error(CommonEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 方法2
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVo exceptionHandler2(Exception e){

        LOGGER.error("未知异常！异常堆栈:", e);
        logRequestInfo();

        return ResultVo.error(CommonEnum.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public ResultVo missingServletRequestParameterException(Exception e){

        LOGGER.error("出现MissingServletRequestParameterException:", e);
        logRequestInfo();

        return ResultVo.error(CommonEnum.INTERNAL_SERVER_ERROR);
    }

    private void logRequestInfo() {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null){
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            LOGGER.info("请求url:{}", request.getRequestURI());
            LOGGER.info("请求源IP:{}", WebUtils.getRealServerIp(request));
        }
    }

}
