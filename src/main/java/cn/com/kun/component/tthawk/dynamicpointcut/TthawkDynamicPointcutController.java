package cn.com.kun.component.tthawk.dynamicpointcut;

import cn.com.kun.component.tthawk.core.MethodKeyExceptionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/8/1
 * desc:
*/
@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@RequestMapping("/tthawk-dpc")
@RestController
public class TthawkDynamicPointcutController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkDynamicPointcutController.class);

    @PostMapping("/attack")
    public String attack(@RequestBody MethodKeyVO methodKeyVO) throws IOException {


        MethodKeyExceptionHolder.add(methodKeyVO.getMethodKey(), methodKeyVO.getExceptionClass());
        if (methodKeyVO.getNestedExceptionClass() != null && methodKeyVO.getNestedExceptionClass().length() > 0){
            MethodKeyExceptionHolder.addNestedException(methodKeyVO.getMethodKey(), methodKeyVO.getNestedExceptionClass());
        }

        return "tthawk dynamicpointcut attack ok";
    }


    @PostMapping("/cancel-attack")
    public String cancelAttack(@RequestBody MethodKeyVO methodKeyVO) throws IOException {


        MethodKeyExceptionHolder.remove(methodKeyVO.getMethodKey());
        return "tthawk dynamicpointcut cancel attack ok";
    }

}
