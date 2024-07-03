package cn.com.kun.config.mysql;

import cn.com.kun.controller.mybatis.multiDatasourceSwitch.GlobalDataSourceLookupSaveContainer;
import cn.com.kun.service.mybatis.extend.multidbswitch.DynamicDataSource;
import cn.com.kun.service.mybatis.extend.multidbswitch.DynamicDataSourceInitHelper;
import cn.com.kun.service.mybatis.extend.multidbswitch.change.CurrentDbSourceQuerier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源配置
 *
 * author:xuyaokun_kzx
 * date:2024/7/2
 * desc:
*/
//@Configuration
public class MultiDataSourceConfig {

    @Autowired
    @Qualifier("mainDataSource")
    private DataSource mainDataSource;

    @Autowired
    @Qualifier("quartzDataSource")
    private DataSource quartzDataSource;

    /**
     * 使用例子
     *
     * @return
     */
    @Primary
    @Bean
    public DataSource multiDataSource() {

        //配置多个数据源
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("primary", mainDataSource);
        dataSourceMap.put("secondary", quartzDataSource);

        CurrentDbSourceQuerier querier = new CurrentDbSourceQuerier() {
            @Override
            public String queryCurrentDbSource() {

                //正常来说，可以查DB，也可以查内存

                return GlobalDataSourceLookupSaveContainer.getCurrentDb();
            }
        };

        DynamicDataSource dynamicDataSource = DynamicDataSourceInitHelper.buildGlobalDynamicDataSource(querier);
        //默认数据源
        dynamicDataSource.setDefaultTargetDataSource(mainDataSource);
        dynamicDataSource.setTargetDataSources(dataSourceMap);

        return dynamicDataSource;
    }


}
