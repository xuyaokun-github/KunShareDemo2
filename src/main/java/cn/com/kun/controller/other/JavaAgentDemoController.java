package cn.com.kun.controller.other;

import cn.com.kun.common.vo.ResultVo;
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

    @GetMapping("/test2")
    public String testString(String a, String b){

        //将会使用agent技术，将这个return改成 return "kunghsu2";
        //原理是将整个testString替换掉
        return "kunghsu";
    }

    @GetMapping("/testInt")
    public int testInt(String a, String b){

        //将会使用agent技术，将这个return改成 return "kunghsu2";
        //原理是将整个testString替换掉
        return 100;
    }

    @GetMapping("/testLong")
    public long testLong(String a, String b){

        //将会使用agent技术，将这个return改成 return "kunghsu2";
        //原理是将整个testString替换掉
        return 101;
    }

    @GetMapping("/testDouble")
    public double testDouble(String a, String b){

        //将会使用agent技术，将这个return改成 return "kunghsu2";
        //原理是将整个testString替换掉
        return 102;
    }

    @GetMapping("/testFloat")
    public float testFloat(String a, String b){

        //将会使用agent技术，将这个return改成 return "kunghsu2";
        //原理是将整个testString替换掉
        return 103;
    }

    @GetMapping("/testResultVo")
    public ResultVo testResultVo(){

        //将会使用agent技术，将这个return改成 return "kunghsu2";
        //原理是将整个testString替换掉7*
        return ResultVo.valueOfSuccess();
    }
}
