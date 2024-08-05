package cn.com.kun.component.tthawk.dynamicmethodmatch;

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
@RequestMapping("/tthawk-dynamicmethodmatch")
@RestController
public class TthawkDynamicMethodMatchController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkDynamicMethodMatchController.class);

    @PostMapping("/attack")
    public String attack(@RequestBody DynamicMethodVO dynamicMethodVO) throws IOException {

        DynamicMethodMatchHolder.add(dynamicMethodVO.getMethodKey(), dynamicMethodVO.getExceptionClass());
        return "tthawk dynamicmethodmatch attack ok";
    }


    @PostMapping("/cancel-attack")
    public String cancelAttack(@RequestBody DynamicMethodVO dynamicMethodVO) throws IOException {

        DynamicMethodMatchHolder.remove(dynamicMethodVO.getMethodKey());
        return "tthawk dynamicmethodmatch attack ok";
    }

}
