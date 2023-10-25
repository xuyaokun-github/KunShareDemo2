package cn.com.kun.component.sentinel.flowmonitor;


/**
 * 注册监控结果持久化实现类
 *
 * author:xuyaokun_kzx
 * date:2021/11/19
 * desc:
*/
public final class StoreMonitorResultServiceRegistry {

    private static StoreMonitorResultService storeMonitorResultService = null;

    /**
     * 注册监控结果保存服务实现
     *
     * @param service
     */
    public static synchronized void registerStoreMonitorResultService(StoreMonitorResultService service) {
        storeMonitorResultService = storeMonitorResultService;
    }

    public static StoreMonitorResultService getStoreMonitorResultService() {
        return storeMonitorResultService;
    }

    private StoreMonitorResultServiceRegistry() {}
}

