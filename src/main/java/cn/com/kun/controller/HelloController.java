package cn.com.kun.controller;

import cn.com.kun.bean.model.people.People;
import cn.com.kun.common.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@RestController
public class HelloController {

    private final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Value("#{'${test.set:}'.empty? null: '${test.set:}'.split(',')}")
    private Set<String> testSet;

    @Value("#{'${test.set2:A;B;C}'.split(';')}")
    private Set<String> testSet2;

    @PostConstruct
    public void init() throws IOException {
        ApplicationContext applicationContext = SpringContextUtil.getContext();
        Resource resource = applicationContext.getResource("classpath:config.txt");
        File file = resource.getFile();
        InputStream inputStream = resource.getInputStream();
//        logger.info(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
//        logger.info(applicationContext.getApplicationName());
        if(testSet != null){
            testSet.size();
        }
        if(testSet2 != null){
            testSet2.size();
        }
    }

    @GetMapping("/hello")
    public String testString(){
        return "kunghsu";
    }

    @GetMapping("/testExclude")
    public People testExclude(){
        People user = new People();
        user.setLastname("xyk");
        user.setFirstname("kunghsu");
        user.setPhone("10086");
        user.setEmail("12306@qq.com");
        int a = 1/0;
        return user;
    }

}
