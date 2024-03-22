package cn.com.kun.framework.apache.skywalking.extend.blacktechnology;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/skywalking-extend")
@RestController
public class SwExtendController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SwExtendController.class);

    /**
     * 供黑科技调用
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/entry-method")
    public String entryMethod() throws InterruptedException {

        //从线程副本里取出runnable执行
        Runnable runnable = SwExtendRunnableHolder.get();
        if (runnable != null){
            runnable.run();
        }

        return "kunghsu";
    }

}
