package cn.com.kun.common.advice;

import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);


    /**
     * 不能重复定义同一个异常，否则启动报错
     *
     * @param ex
     * @return
     */
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
    public ResultVo handleEx(HttpServletRequest req, Exception ex){

        LOGGER.error("业务异常", ex);
        return ResultVo.valueOfError("业务异常");
    }

}
