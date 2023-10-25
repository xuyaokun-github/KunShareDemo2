package cn.com.kun.springframework.springcloud.alibaba.sentinel.demo.kafkademo;

import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.component.sentinel.properties.SentinelRuleProperties;
import cn.com.kun.component.sentinel.vo.CustomFlowRule;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.CONTEXT_MSG_PUSH;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/10/24
 * desc:
*/
@Service
public class KscInvokeDownStreamDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(KscInvokeDownStreamDemoService.class);


    @Autowired
    private SentinelRuleProperties sentinelRuleProperties;

    @PostConstruct
    public void init(){

    }

    /**
     * 简单限流
     * 该方法会被疯狂访问，暂时先不打日志
     */
    public String invokeDownStream(String channel, String platform){

        //标记调用链路
        ContextUtil.enter(CONTEXT_MSG_PUSH);
        //根据channel 找到对应的资源名称
        String resouceName = matchFlowResource(channel, platform);

        try (Entry entry = SphU.entry(resouceName)) {
            //被保护的逻辑
            doInvokeDownStream();

        } catch (BlockException ex) {
            // 处理被流控的逻辑
            //com.alibaba.csp.sentinel.slots.block.flow.FlowException
//            LOGGER.error("触发限流", ex);
        }

        return ThreadUtils.getCurrentInvokeClassAndMethod() + "执行完毕";
    }

    /**
     * 找到合适的资源名称
     *
     * @param channel
     * @param platform
     * @return
     */
    private String matchFlowResource(String channel, String platform) {

        Map<String, CustomFlowRule> flowRuleMap = sentinelRuleProperties.getFlowRule();
        String resourceName = String.join("_", CONTEXT_MSG_PUSH, channel);
        if (flowRuleMap != null && flowRuleMap.containsKey(resourceName)){
            return resourceName;
        }
        return String.join("_", CONTEXT_MSG_PUSH, platform);
    }


    private void doInvokeDownStream(){

        //具体的调用下游逻辑

    }


}
