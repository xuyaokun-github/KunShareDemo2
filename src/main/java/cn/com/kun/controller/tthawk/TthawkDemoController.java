package cn.com.kun.controller.tthawk;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.tthawk.TthawkDemoService;
import cn.com.kun.service.tthawk.TthawkDemoVO1;
import cn.com.kun.service.tthawk.TthawkDemoVO2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/tthawk-demo")
@RestController
public class TthawkDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkDemoController.class);

    @Autowired
    private TthawkDemoService tthawkDemoService;

    @GetMapping("/test")
    public ResultVo test() throws IOException {

        tthawkDemoService.test();
        return ResultVo.valueOfSuccess("OK");
    }

    @GetMapping("/test2")
    public ResultVo test2() throws IOException {

        tthawkDemoService.test2(new TthawkDemoVO1(), new TthawkDemoVO2());
        return ResultVo.valueOfSuccess("OK");
    }

    @GetMapping("/test3")
    public ResultVo test3() throws IOException {

        tthawkDemoService.test3();
        return ResultVo.valueOfSuccess("OK");
    }
}
