package cn.com.kun.service.mybatis.extend.multidbswitch.lookup;

/**
 * 选中的数据源放在线程副本中
 *
 * author:xuyaokun_kzx
 * date:2024/7/2
 * desc:
*/
public class ThreadDataSourceLookupStrategy implements DataSourceLookupStrategy {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public String getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }

}
