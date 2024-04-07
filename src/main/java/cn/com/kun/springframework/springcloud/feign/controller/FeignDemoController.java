package cn.com.kun.springframework.springcloud.feign.controller;

import cn.com.kun.common.utils.HttpVisitUtil;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.feign.client.KunShareDemo2Feign;
import cn.com.kun.springframework.springcloud.feign.client.KunwebdemoFeign;
import cn.com.kun.springframework.springcloud.feign.client.KunwebdemoFeign2;
import cn.com.kun.springframework.springcloud.feign.extend.EurekaInstanceDiscover;
import cn.com.kun.springframework.springcloud.feign.extend.MicroserviceInstanceDiscover;
import cn.com.kun.springframework.springcloud.feign.service.KunShareClientOneFeignService;
import cn.com.kun.springframework.springcloud.feign.vo.FeignJacksonDO;
import cn.com.kun.springframework.springcloud.feign.vo.FeignJacksonResVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequestMapping("/feign-demo")
@RestController
public class FeignDemoController {

    private final static Logger logger = LoggerFactory.getLogger(FeignDemoController.class);

    @Autowired
    private KunShareClientOneFeignService kunShareClientOneFeignService;

    @Autowired
    private KunwebdemoFeign kunwebdemoFeign;

    @Autowired
    private KunwebdemoFeign2 kunwebdemoFeign2;

    @Autowired
    private KunShareDemo2Feign kunShareDemo2Feign;

    @Autowired
    private MicroserviceInstanceDiscover microserviceInstanceDiscover;

    @Autowired(required = false)
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

    @GetMapping("/test-allow-bean-definition-overriding")
    public ResultVo testAllowBeanDefinitionOverriding(){

        ResultVo resultVo = kunwebdemoFeign.result1("xyk");
        ResultVo resultVo2 = kunwebdemoFeign2.result1("xyk");
        logger.info("resultVo:{}", JacksonUtils.toJSONString(resultVo));
        logger.info("resultVo2:{}", JacksonUtils.toJSONString(resultVo2));

        return ResultVo.valueOfSuccess();
    }


    @GetMapping("/test-jackson")
    public ResultVo testJacksonServer() throws InterruptedException {

        FeignJacksonResVO feignJacksonResVO = new FeignJacksonResVO();
        feignJacksonResVO.setUuid(UUID.randomUUID().toString());
        feignJacksonResVO.setCreateTime(new Date());

        /*
            反例代码：返回FeignJacksonDO 并且createTime没有指定@JsonFormat注解
            这个代码在springboot1.5.15没问题，但是在2.7有问题
         */
        FeignJacksonDO feignJacksonDO = new FeignJacksonDO();
        BeanUtils.copyProperties(feignJacksonResVO, feignJacksonDO);

        return ResultVo.valueOfSuccess(feignJacksonDO);
    }


    /**
     * jackson 升级springboot2.7.12问题
     * @return
     */
    @GetMapping("/testJackson")
    public ResultVo testJackson(){

        //请求1.5.15版本的服务端
        ResultVo resultVo = kunwebdemoFeign.testJackson();
        logger.info("resultVo:{}", JacksonUtils.toJSONString(resultVo));

        return ResultVo.valueOfSuccess();
    }

    /**
     * jackson 升级springboot2.7.12问题
     * @return
     */
    @GetMapping("/testJackson2")
    public ResultVo testJackson2(){

        //请求2.7版本的服务端
        ResultVo resultVo = kunShareDemo2Feign.testJackson();
        logger.info("resultVo:{}", JacksonUtils.toJSONString(resultVo));

        return ResultVo.valueOfSuccess();
    }

    /**
     * 供kunwebdemo调用
     *
     * @param req
     * @return
     */
    @PostMapping("/post-method")
    public ResultVo postMethod(ResultVo req){

        logger.info("=========================================================post-method in kunsharedemo, receive:{}", JacksonUtils.toJSONString(req));
        return ResultVo.valueOfSuccess("I am kunsharedemo post-method");
    }

    /**
     * get方法带参数
     * @return
     */
    @GetMapping("/testPostProblem")
    public ResultVo testPostProblem(){

        //请求2.7版本的服务端
        ResultVo resultVo = kunwebdemoFeign.postMethod(ResultVo.valueOfSuccess("req"));
//        ResultVo resultVo = kunwebdemoFeign.postMethod2();
        logger.info("result:{}", resultVo);
        return ResultVo.valueOfSuccess();
    }

    /**
     * get方法不带参数
     *
     * @return
     */
    @GetMapping("/testPostProblemNoParam")
    public ResultVo testPostProblemNoParam(){

        //请求2.7版本的服务端
//        ResultVo resultVo = kunwebdemoFeign.postMethod(ResultVo.valueOfSuccess());
        ResultVo resultVo = kunwebdemoFeign.postMethod2();
        logger.info("result:{}", resultVo);
        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/testGetProblem")
    public ResultVo testGetProblem(){

        //请求2.7版本的服务端
//        ResultVo resultVo = kunwebdemoFeign.postMethod(ResultVo.valueOfSuccess());
        ResultVo resultVo = kunwebdemoFeign.getMethod();
        logger.info("result:{}", resultVo);
        return ResultVo.valueOfSuccess();
    }

    /**
     * 没法复现Broken Pipe异常
     * （强制关停服务，能复现）
     * @return
     */
    @GetMapping("/testBrokenPipeProblem")
    public ResultVo testBrokenPipeProblem(){

        //请求2.7版本的服务端
        logger.info("testBrokenPipeProblem开始调用");
        ResultVo resultVo = kunwebdemoFeign.getMethod();
        logger.info("result:{}", resultVo);
        return ResultVo.valueOfSuccess();
    }

    /**
     * 使用HttpURLConnection 也没法复现BrokenPipe异常
     * （强制关停服务，能复现）
     * @return
     */
    @GetMapping("/testBrokenPipeProblem2")
    public ResultVo testBrokenPipeProblem2(){

        //请求2.7版本的服务端
        logger.info("testBrokenPipeProblem开始调用");
        HttpVisitUtil.doByGet("http://localhost:8091/kunwebdemo/feign-demo/get-method", new HashMap<>());
        logger.info("result:{}", "");
        return ResultVo.valueOfSuccess();
    }

    /**
     * 加了网关进来，也没法复现BrokenPipe异常
     * （强制关停服务，能复现）
     * @return
     */
    @GetMapping("/testBrokenPipeProblem2WithGateway")
    public ResultVo testBrokenPipeProblem2WithGateway(){

        //请求2.7版本的服务端
        logger.info("testBrokenPipeProblem开始调用");
        HttpVisitUtil.doByGet("http://localhost:8089/kunshare-zuul/kunwebdemo/kunwebdemo/feign-demo/get-method", new HashMap<>());
        logger.info("result:{}", "");
        return ResultVo.valueOfSuccess();
    }
}
