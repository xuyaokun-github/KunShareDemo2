package cn.com.kun.component.tthawk.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@RequestMapping("/tthawk-second")
@RestController
public class TthawkSecondController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkSecondController.class);

    @Autowired
    private ApplicationContext context;

    @PostMapping("/attack")
    public String attack(@RequestBody ReflectVO reflectVO) throws IOException {

        reflectInit(reflectVO);
        ReflectInvokeUtils.doAttack(reflectVO);

        return "tthawk attack ok";
    }

    private void reflectInit(ReflectVO reflectVO) {

        if (AcquireBeanMode.SPRING.getMode().equals(reflectVO.getAcquireBeanMode())){
            Object bean = SpringBeanUtils.getBean(reflectVO.getClassName(), reflectVO.getSpringBeanName());
            reflectVO.setSpringBean(bean);
        }

    }


}
