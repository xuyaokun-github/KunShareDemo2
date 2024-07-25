package cn.com.kun.controller.tthawk;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.tthawk.TthawkDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
