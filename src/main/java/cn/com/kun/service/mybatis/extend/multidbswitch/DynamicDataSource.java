package cn.com.kun.service.mybatis.extend.multidbswitch;

import cn.com.kun.service.mybatis.extend.multidbswitch.lookup.DataSourceLookupStrategyFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 * author:xuyaokun_kzx
 * date:2024/7/2
 * desc:
*/
public class DynamicDataSource extends AbstractRoutingDataSource {

    private String holderType;

    public DynamicDataSource() {

    }

    public DynamicDataSource(String holdeType) {
        this.holderType = holdeType;
    }

    @Override
    protected Object determineCurrentLookupKey() {

        return DataSourceLookupStrategyFactory.getByHolderType(holderType).getDataSourceType();
    }

}
