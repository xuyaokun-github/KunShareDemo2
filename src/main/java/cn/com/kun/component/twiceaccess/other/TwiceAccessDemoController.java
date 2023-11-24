package cn.com.kun.component.twiceaccess.other;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.twiceaccess.annotation.TwiceAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 二次访问组件（TwiceAccess）
 *
 * author:xuyaokun_kzx
 * date:2023/11/21
 * desc:
*/
@RequestMapping("/twiceaccess")
@RestController
public class TwiceAccessDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TwiceAccessDemoController.class);

    /**
     * 通过Header的方式传递二次访问标识
     * @return
     */
    @TwiceAccess(waitTime = 3000)
    @PostMapping("/test1")
    public ResultVo test1(@RequestBody TwiceAccessDemoReq twiceAccessDemoReq) throws InterruptedException {

        LOGGER.info("test1 result:{}", JacksonUtils.toJSONString(twiceAccessDemoReq));
        TwiceAccessDemoRes res = new TwiceAccessDemoRes();
        res.setResString(twiceAccessDemoReq.getQueryParam());
        res.setResType("888");
        Thread.sleep(1 * 60 * 1000);
        return ResultVo.valueOfSuccess(res);
    }

    /**
     * 通过请求参数的方式传递二次访问标识
     * @return
     */
    @TwiceAccess(waitTime = 6000)
    @GetMapping("/test2")
    public ResultVo test2(){

        LOGGER.info("test2 result:{}", JacksonUtils.toJSONString(ResultVo.valueOfSuccess()));
        TwiceAccessDemoRes res = new TwiceAccessDemoRes();
        res.setResString(UUID.randomUUID().toString());
        res.setResType("999");
        return ResultVo.valueOfSuccess(res);
    }

    @TwiceAccess(waitTime = 3000)
    @PostMapping("/test3")
    public ResultVo test3(@RequestBody TwiceAccessDemoReq twiceAccessDemoReq) throws InterruptedException {

        LOGGER.info("test3 result:{}", JacksonUtils.toJSONString(twiceAccessDemoReq));

        if (true){
            throw new RuntimeException("XXX业务参数缺失");
        }

        TwiceAccessDemoRes res = new TwiceAccessDemoRes();
        res.setResString(twiceAccessDemoReq.getQueryParam());
        res.setResType("888");
        Thread.sleep(1 * 60 * 1000);
        return ResultVo.valueOfSuccess(res);
    }


}
