package cn.com.kun.springframework.core.ioc.beanoverride;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 使用不同类型的类，无法复现spring.main.allow-bean-definition-overriding报错。
 *
 * author:xuyaokun_kzx
 * date:2023/11/17
 * desc:
*/
@RequestMapping("/spring-bean-override")
@RestController
public class SpringBeanOverrideDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringBeanOverrideDemoController.class);

//    @Resource
//    @Autowired
    SpringBeanOverrideDemoServiceBase springBeanOverrideDemoService;

//    @Resource
//    @Autowired
    SpringBeanOverrideDemoServiceBase springBeanOverrideDemoService2;

    @Autowired
    CharacterEncodingFilter myCharacterEncodingFilter;

    @GetMapping("/test1")
    public ResultVo<User> test1(){

        String encoding = myCharacterEncodingFilter.getEncoding();
        LOGGER.info("encoding:{}", encoding);

//        LOGGER.info("springBeanOverrideDemoService:{}", springBeanOverrideDemoService.name());
        return ResultVo.valueOfSuccess();
    }


    @GetMapping("/test2")
    public ResultVo<User> test2(){

        LOGGER.info("springBeanOverrideDemoService2:{}", springBeanOverrideDemoService2.name());
        return ResultVo.valueOfSuccess(springBeanOverrideDemoService2.name());
    }

}
