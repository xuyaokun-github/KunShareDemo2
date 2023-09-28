package cn.com.kun.controller.other;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Java agent demo
 *
 * author:xuyaokun_kzx
 * date:2023/9/28
 * desc:
*/
@RestController
@RequestMapping("/agent-demo")
public class JavaAgentDemoController {

    @GetMapping("/test")
    public String testString(){

        //将会使用agent技术，将这个return改成 return "kunghsu2";
        //原理是将整个testString替换掉
        return "kunghsu";
    }


}
