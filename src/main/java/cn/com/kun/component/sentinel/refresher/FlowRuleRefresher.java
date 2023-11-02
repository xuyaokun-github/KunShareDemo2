package cn.com.kun.component.sentinel.refresher;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 限流规则动态刷新器
 *
 * author:xuyaokun_kzx
 * date:2023/10/20
 * desc:
*/
@Component
public class FlowRuleRefresher {

    private final static Logger LOGGER = LoggerFactory.getLogger(FlowRuleRefresher.class);


    public boolean refresh(String resource, double count) {

        //是否需要刷新
        boolean needRefresh = false;
        try {
            List<FlowRule> rules = FlowRuleManager.getRules();
            List<FlowRule> newRules = new ArrayList<>();
            for (FlowRule flowRule : rules){
                FlowRule newFlowRule  = new FlowRule();
                newFlowRule.setResource(flowRule.getResource());
                newFlowRule.setControlBehavior(flowRule.getControlBehavior());
                newFlowRule.setCount(flowRule.getCount());
                if (flowRule.getResource().equals(resource) && flowRule.getCount() != count){
                    //限流值不相同时，才有必要调用刷新(必须重建对象，否则框架认为限流规则没变，因为底层实现了equals方法)
                    newFlowRule.setCount(count);
                    needRefresh = true;
                }
                newRules.add(newFlowRule);
            }

            if (needRefresh){
                //加载（注意，这里假如只添加了待更新的规则，不添加已经加载的规则，会导致已加载的规则被移除）
                FlowRuleManager.loadRules(newRules);
                LOGGER.info("动态刷新sentinel限流值成功，本次刷新resource：{} 限流值:{}", resource, count);
            }
        }catch (Exception e){
            LOGGER.error("动态刷新sentinel限流值异常", e);
        }

        return true;
    }

}
