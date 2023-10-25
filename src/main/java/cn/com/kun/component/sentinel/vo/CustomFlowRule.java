package cn.com.kun.component.sentinel.vo;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

/**
 * 限流规则的扩展
 * 继承自com.alibaba.csp.sentinel.slots.block.flow.FlowRule
 *
 * author:xuyaokun_kzx
 * date:2021/10/14
 * desc:
*/
public class CustomFlowRule extends FlowRule {

    /**
     * 黄线阈值
     */
    private Long yellowLineThreshold;

    public Long getYellowLineThreshold() {
        return yellowLineThreshold;
    }

    public void setYellowLineThreshold(Long yellowLineThreshold) {
        this.yellowLineThreshold = yellowLineThreshold;
    }
}
