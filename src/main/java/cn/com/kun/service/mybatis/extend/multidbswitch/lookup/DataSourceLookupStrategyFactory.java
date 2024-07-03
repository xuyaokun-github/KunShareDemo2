package cn.com.kun.service.mybatis.extend.multidbswitch.lookup;

import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceLookupStrategyFactory {

    private static Map<String, DataSourceLookupStrategy> services = new ConcurrentHashMap<String, DataSourceLookupStrategy>();

    public static DataSourceLookupStrategy getByHolderType(String type){
        return services.get(type);
    }

    public static void register(String holderType, DataSourceLookupStrategy dataSourceLookupStrategy){
        Assert.notNull(holderType,"holderType can't be null");
        services.put(holderType,dataSourceLookupStrategy);
    }

}
