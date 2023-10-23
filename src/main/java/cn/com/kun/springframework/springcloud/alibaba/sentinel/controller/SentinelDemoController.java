package cn.com.kun.springframework.springcloud.alibaba.sentinel.controller;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.service.SentinelDemoService;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.service.flowMonitor.SentinelExtendService;
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

}
