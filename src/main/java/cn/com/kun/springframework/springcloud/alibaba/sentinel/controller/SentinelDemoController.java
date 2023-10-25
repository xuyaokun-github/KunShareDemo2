package cn.com.kun.springframework.springcloud.alibaba.sentinel.controller;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.service.SentinelDemoService;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.service.commandHandler.NoFastjsonFetchJsonTreeCommandHandler;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.service.flowMonitor.SentinelExtendService;
import com.alibaba.csp.sentinel.command.CommandHandler;
import com.alibaba.csp.sentinel.transport.command.SimpleHttpCommandCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sentinel-demo")
@RestController
public class SentinelDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SentinelDemoController.class);


    @Autowired
    private SentinelExtendService sentinelExtendService;

    @Autowired
    private SentinelDemoService sentinelDemoService;

    @GetMapping("/testGetAllJsonInfo")
    public String testGetAllJsonInfo() throws Exception {

        return JacksonUtils.toJSONString(sentinelExtendService.getAllJsonInfo());
    }

    @GetMapping("/testChangeSleepMillis")
    public String testChangeSleepMillis(@RequestParam("sleep") String sleep) throws Exception {

        sentinelDemoService.changeSleepMillis(sleep);
        return "OK";
    }

    /**
     * 验证移除fastjson后，jsonTree之类的Handler是否能正常工作
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/testJsonTreeHandler")
    public String testJsonTreeHandler() throws Exception {


        //触发 名字为 jsonTree 的handler
        return JacksonUtils.toJSONString(sentinelExtendService.getAllJsonInfoByFetchJsonTreeCommandHandler());
    }

    @GetMapping("/testReplaceJsonTreeHandler")
    public ResultVo testReplaceJsonTreeHandler() throws Exception {

        CommandHandler<?> commandHandler = SimpleHttpCommandCenter.getHandler("noFastjsonJsonTree");

        SimpleHttpCommandCenter.registerCommand("jsonTree", new NoFastjsonFetchJsonTreeCommandHandler());
        return ResultVo.valueOfSuccess();
    }

}
