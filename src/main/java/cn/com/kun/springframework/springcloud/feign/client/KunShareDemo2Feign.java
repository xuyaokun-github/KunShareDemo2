package cn.com.kun.springframework.springcloud.feign.client;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.feign.vo.FeignJacksonResVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 展示一个不走注册中心的例子
 * 这个模拟的就是调用第三方接口的场景
 * author:xuyaokun_kzx
 * date:2021/5/26
 * desc:
*/
@FeignClient(name = "kunsharedemo27", url = "http://127.0.0.1:8082") //本地方式
//@FeignClient(name = "kunsharedemo27") //注册中心方式
public interface KunShareDemo2Feign {

    /**
     * Get请求-不带参数
     * @return
     */
//    @GetMapping("/feigndemo/test") //错误示例，因为少了上下文，服务端那边定义了上下文，则接口这里必须加上下文
    @GetMapping("/kunsharedemo27/feign/test1")
    ResultVo result();


    @GetMapping("/kunsharedemo27/feign-demo/test-jackson")
    ResultVo<FeignJacksonResVO> testJackson();


    @PostMapping("/kunsharedemo27/user-demo/testBomEncodingForPost")
    ResultVo<String> testBomEncodingForPost(@RequestBody String firstname);


}
