package cn.com.kun.component.sentinel.flowmonitor;

import cn.com.kun.component.sentinel.vo.FlowMonitorRes;

public interface StoreMonitorResultService {

    /**
     * 保存监控结果
     * @param flowMonitorRes
     */
    void storeMonitorResult(FlowMonitorRes flowMonitorRes);

}
