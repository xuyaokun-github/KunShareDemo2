package cn.com.kun.service.mybatis.extend.multidbswitch;

import cn.com.kun.service.mybatis.extend.multidbswitch.change.CurrentDbSourceQuerier;
import cn.com.kun.service.mybatis.extend.multidbswitch.change.GlobalChangeDataSourceSchedule;
import cn.com.kun.service.mybatis.extend.multidbswitch.enums.HolderTypeEnum;
import cn.com.kun.service.mybatis.extend.multidbswitch.lookup.DataSourceLookupStrategyFactory;
import cn.com.kun.service.mybatis.extend.multidbswitch.lookup.GlobalDataSourceLookupStrategy;
import cn.com.kun.service.mybatis.extend.multidbswitch.lookup.ThreadDataSourceLookupStrategy;

/**
 * 上游使用入口
 *
 * author:xuyaokun_kzx
 * date:2024/7/3
 * desc:
*/
public class DynamicDataSourceInitHelper {

    /**
     * 创建数据源
     *
     * @return
     * @param querier
     */
    public static DynamicDataSource buildGlobalDynamicDataSource(CurrentDbSourceQuerier querier) {

        String holderType = HolderTypeEnum.GLOBAL.getHolderType();
        DynamicDataSource dynamicDataSource = new DynamicDataSource(holderType);
        //注册实现
        DataSourceLookupStrategyFactory.register(holderType, new GlobalDataSourceLookupStrategy());
        //开启调度

        //注册查询当前数据源的逻辑
        GlobalChangeDataSourceSchedule schedule = new GlobalChangeDataSourceSchedule();
        schedule.setQuerier(querier);
        schedule.init();

        return dynamicDataSource;
    }

    public static DynamicDataSource buildThreadDynamicDataSource() {

        String holderType = HolderTypeEnum.THREAD.getHolderType();
        DynamicDataSource dynamicDataSource = new DynamicDataSource(holderType);
        DataSourceLookupStrategyFactory.register(holderType, new ThreadDataSourceLookupStrategy());
        return dynamicDataSource;
    }


}
