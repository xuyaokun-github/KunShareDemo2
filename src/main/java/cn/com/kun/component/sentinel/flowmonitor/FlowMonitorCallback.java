package cn.com.kun.component.sentinel.flowmonitor;

import cn.com.kun.component.sentinel.vo.FlowMonitorRes;

@FunctionalInterface
public interface FlowMonitorCallback {

    /**
     * 监控后回调
     * @param flowMonitorRes
     */
    void monitorCallback(FlowMonitorRes flowMonitorRes);

}
