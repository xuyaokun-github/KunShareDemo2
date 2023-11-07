package cn.com.kun.springframework.springcloud.feign.controller;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.feign.client.KunwebdemoFeign;
import cn.com.kun.springframework.springcloud.feign.extend.EurekaInstanceDiscover;
import cn.com.kun.springframework.springcloud.feign.extend.MicroserviceInstanceDiscover;
import cn.com.kun.springframework.springcloud.feign.service.KunShareClientOneFeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/feign")
@RestController
public class FeignDemoController {

    private final static Logger logger = LoggerFactory.getLogger(FeignDemoController.class);

    @Autowired
    private KunShareClientOneFeignService kunShareClientOneFeignService;

    @Autowired
    private KunwebdemoFeign kunwebdemoFeign;

    @Autowired
    private MicroserviceInstanceDiscover microserviceInstanceDiscover;

    @Autowired
    private EurekaInstanceDiscover eurekaInstanceDiscover;

    @GetMapping("/test")
    public ResultVo test(){

        ResultVo res = kunShareClientOneFeignService.result();
        logger.info("result:{}", JacksonUtils.toJSONString(res));
        return res;
    }

    @GetMapping("/test1")
    public ResultVo test1(){

        logger.info("result:{}", JacksonUtils.toJSONString(ResultVo.valueOfSuccess()));
        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/test-kunwebdemo")
    public ResultVo testKunwebdemo(){

        ResultVo res = kunwebdemoFeign.result();
        logger.info("result:{}", JacksonUtils.toJSONString(res));
//        People people = new People();
//        people.setFirstname("tracy");
//        res = otherClientFeignService.result2(people);
//        logger.info("result:{}", JacksonUtils.toJSONString(res));
//        res = otherClientFeignService.result3(people, "myheader-source");
//        logger.info("result:{}", JacksonUtils.toJSONString(res));
        return res;
    }

    @GetMapping("/testMicroserviceInstanceDiscover")
    public ResultVo testMicroserviceInstanceDiscover(){

//        List<String> hostPorts = microserviceInstanceDiscover.findAllIPAddrs("kunwebdemo");
//        logger.info("找到的微服务IP地址:{}", JacksonUtils.toJSONString(hostPorts));
//        List<String> hostnames = microserviceInstanceDiscover.findAllHostnames("kunwebdemo");
//        logger.info("找到的微服务主机名:{}", JacksonUtils.toJSONString(hostnames));
        List<String> hostPorts = microserviceInstanceDiscover.findAllIPAddrs("kunsharedemo27");
        logger.info("找到的微服务IP地址:{}", JacksonUtils.toJSONString(hostPorts));

        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/testEurekaInstanceDiscover")
    public ResultVo testEurekaInstanceDiscover(){

//        List<String> hostPorts = microserviceInstanceDiscover.findAllIPAddrs("kunwebdemo");
//        logger.info("找到的微服务IP地址:{}", JacksonUtils.toJSONString(hostPorts));
//        List<String> hostnames = microserviceInstanceDiscover.findAllHostnames("kunwebdemo");
//        logger.info("找到的微服务主机名:{}", JacksonUtils.toJSONString(hostnames));

        List<String> hostPorts2 = eurekaInstanceDiscover.findAllIPAddrs("kunwebdemo");
        logger.info("找到的微服务IP地址:{}", JacksonUtils.toJSONString(hostPorts2));

        return ResultVo.valueOfSuccess();
    }


    @GetMapping("/testEureka-getServices")
    public ResultVo testEurekaGetServices(){

        List<String> services = eurekaInstanceDiscover.getServices();
        logger.info("找到的微服务名:{}", JacksonUtils.toJSONString(services));

        return ResultVo.valueOfSuccess();
    }
}
