package cn.com.kun.springframework.springcloud.feign.client;

import cn.com.kun.common.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 展示一个不走注册中心的例子
 * 这个模拟的就是调用第三方接口的场景
 * author:xuyaokun_kzx
 * date:2021/5/26
 * desc:
*/
@FeignClient(name = "kunwebdemo2", url = "http://127.0.0.1:8091")
//@FeignClient(name = "kunwebdemo") //假如不指定url,则走注册中心
public interface KunwebdemoFeign2 {


    /**
     * Get请求-带参数
     * @param name
     * @return
     */
    @GetMapping("/kunwebdemo/feigndemo/test")
    ResultVo result1(@RequestParam("name") String name);


}
