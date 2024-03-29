package cn.com.kun.springframework;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.utils.SpringContextUtil;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.core.applicationContextInitializer.ApplicationContextInitializerDemoBean;
import cn.com.kun.springframework.core.beanDefinition.BeanDefinitionDemoService;
import cn.com.kun.springframework.core.binder.NbaplayBinderDemo;
import cn.com.kun.springframework.core.orderComparator.OrderComparatorDemoServcie;
import cn.com.kun.springframework.springcloud.feign.vo.FeignJacksonDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.LiveBeansView;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequestMapping("/springdemo")
@RestController
public class SpringDemoController {

    private final static Logger logger = LoggerFactory.getLogger(SpringDemoController.class);

    @Value("${nbaplay.level}")
    private String nbaplayLevel;

    @Autowired
    BeanDefinitionDemoService beanDefinitionDemoService;

    @Autowired
    NbaplayBinderDemo nbaplayBinderDemo;

    @Autowired
    OrderComparatorDemoServcie orderComparatorDemoServcie;

    @Autowired
    ApplicationContextInitializerDemoBean applicationContextInitializerDemoBean;

    @Autowired
    Environment environment;

    @PostConstruct
    public void init() throws IOException {
        /**
         * 获取classpath下的文件内容
         * 但这种写法，在文件被打进jar包后会读取失败
         */
        ApplicationContext applicationContext = SpringContextUtil.getContext();
        Resource resource = applicationContext.getResource("classpath:config.txt");
        File file = resource.getFile();
        InputStream inputStream = resource.getInputStream();
        logger.info(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
    }

    /**
     * 分析一个@JsonFormat失效的问题
     * @return
     */
    @GetMapping("/testJsonFormat")
    public ResultVo<User> testJsonFormat(){

        User user = new User();
        user.setCreateTime(new Date());
        return ResultVo.valueOfSuccess(user);
    }

    /**
     * 分析一个@JsonFormat失效的问题
     * @return
     */
    @GetMapping("/testJsonFormat2")
    public ResultVo<List<User>> testJsonFormat2(){

        User user = new User();
        user.setCreateTime(new Date());
        List<User> userList = new ArrayList<>();
        userList.add(user);
        return ResultVo.valueOfSuccess(userList);
    }


    @GetMapping("/testBeanDefinition")
    public ResultVo testBeanDefinition(){

        beanDefinitionDemoService.method();
        return ResultVo.valueOfSuccess(null);
    }

    @GetMapping("/testShowAllFactories")
    public ResultVo testShowAllFactories(){

        //    spring.factories文件

        List<String> stringList = SpringFactoriesLoader.loadFactoryNames(ApplicationContextInitializer.class, Thread.currentThread().getContextClassLoader());
        logger.info("SpringFactoriesLoader.loadFactoryNames:{}", JacksonUtils.toJSONString(stringList));
        return ResultVo.valueOfSuccess(null);
    }

    @GetMapping("/testBinder")
    public ResultVo testBinder(){

        nbaplayBinderDemo.method();
        return ResultVo.valueOfSuccess(null);
    }

    @GetMapping("/testOrderComparator")
    public ResultVo testOrderComparator(){

        orderComparatorDemoServcie.method();
        return ResultVo.valueOfSuccess(null);
    }

    /**
     * 验证通过ApplicationContextInitializer注册单例bean
     * @return
     */
    @GetMapping("/testApplicationContextInitializerDemoBean")
    public ResultVo testApplicationContextInitializerDemoBean(){

        applicationContextInitializerDemoBean.show();
        return ResultVo.valueOfSuccess(null);
    }

    @GetMapping("/testLiveBeansView")
    public String testLiveBeansView(){

        String res = new LiveBeansView().getSnapshotAsJson();
        return res;
    }

    @GetMapping("/testJacksonDateSerialize")
    public String testJacksonDateSerialize(){

        FeignJacksonDO feignJacksonDO = new FeignJacksonDO();
        feignJacksonDO.setCreateTime(new Date());
        feignJacksonDO.setUuid(UUID.randomUUID().toString());

        ObjectMapper mapper = new ObjectMapper();
        String string = null;
        try {
            string = mapper.writeValueAsString(feignJacksonDO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        String string = JacksonUtils.toJSONString(feignJacksonDO);
        System.out.println(string);
        return string;
    }



}
