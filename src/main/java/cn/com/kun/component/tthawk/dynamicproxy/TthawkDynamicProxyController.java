package cn.com.kun.component.tthawk.dynamicproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@RequestMapping("/tthawk-dynamicproxy")
@RestController
public class TthawkDynamicProxyController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkDynamicProxyController.class);


    @Autowired
    private ApplicationContext context;

    @PostMapping("/proxy")
    public String proxy(@RequestBody ProxyKeyVO proxyKeyVO) throws Exception {

        TthawkDynamicProxyModifier.proxy(context, proxyKeyVO.getBeanClassName());

        if (proxyKeyVO.getExceptionClass() != null && proxyKeyVO.getExceptionClass().length() > 0){
            DynamicProxyKeyHolder.add(proxyKeyVO.getMethodKey(), proxyKeyVO.getExceptionClass());
        }

        return "tthawk proxy ok";
    }


    @PostMapping("/cancel-attack")
    public String cancelAttack(@RequestBody ProxyKeyVO proxyKeyVO) throws Exception {

        DynamicProxyKeyHolder.remove(proxyKeyVO.getMethodKey());
        TthawkDynamicProxyModifier.removeProxy(context, proxyKeyVO.getBeanClassName());
        return "tthawk dynamicproxy cancel attack ok";
    }
}
