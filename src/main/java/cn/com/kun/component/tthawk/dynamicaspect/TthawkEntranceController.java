package cn.com.kun.component.tthawk.dynamicaspect;

import cn.com.kun.service.tthawk.TthawkThirdDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@RequestMapping("/tthawk-entrance")
@RestController
public class TthawkEntranceController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkEntranceController.class);

    @Autowired
    private DynamicAspectProcessor dynamicAspectProcessor;

    @Autowired
    private ApplicationContext context;

    @PostMapping("/aop")
    public String aop(@RequestBody TthawkReq tthawkReq) throws IOException {

        //动态插桩
        dynamicAspectProcessor.registerAspect("tthawkDynamicAspect");

        return "tthawk ok";
    }


    @GetMapping("/delete-aop")
    public String deleteAop() throws IOException {

        //动态插桩
        dynamicAspectProcessor.unregisterAspect("tthawkDynamicAspect");

        return "tthawk ok";
    }

    @GetMapping("/delete2")
    public String delete2() throws IOException {

        dynamicAspectProcessor.unregisterAspect("tthawkSecondDemoService");
        dynamicAspectProcessor.unregisterAspect("tthawkDemoService");

        return "tthawk ok";
    }

    @GetMapping("/modify")
    public String modify() throws Exception {

        AspectModifier.modifyAspect(context, TthawkThirdDemoService.class, TthawkDynamicAspect.class);
        return "tthawk ok";
    }


}
