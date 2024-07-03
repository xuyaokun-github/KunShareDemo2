package cn.com.kun.service.mybatis.extend.multidbswitch.change;

import cn.com.kun.service.mybatis.extend.multidbswitch.enums.HolderTypeEnum;
import cn.com.kun.service.mybatis.extend.multidbswitch.lookup.DataSourceLookupStrategy;
import cn.com.kun.service.mybatis.extend.multidbswitch.lookup.DataSourceLookupStrategyFactory;
import cn.com.kun.service.mybatis.extend.multidbswitch.lookup.GlobalDataSourceLookupStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * author:xuyaokun_kzx
 * date:2024/7/2
 * desc:
*/
public class GlobalChangeDataSourceSchedule {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalChangeDataSourceSchedule.class);

    private ScheduledExecutorService executorPool = Executors.newScheduledThreadPool(1);

    /**
     * 调度频率，单位：秒
     */
    private Long scheduleRate = 30L;

    private CurrentDbSourceQuerier querier;

    //初始化
    public void init(){

        executorPool.scheduleAtFixedRate(new ChangeDataSourceTask(), 1, scheduleRate, TimeUnit.SECONDS);
    }

    /**
     * 扩展点：查当前数据源的逻辑
     *
     * @param querier
     */
    public void setQuerier(CurrentDbSourceQuerier querier) {
        this.querier = querier;
    }

    /**
     * 上游可自定义频率
     * @param scheduleRate
     */
    public void setScheduleRate(Long scheduleRate) {
        this.scheduleRate = scheduleRate;
    }

    private final class ChangeDataSourceTask implements Runnable {

        ChangeDataSourceTask() {

        }

        @Override
        public void run() {
            try {
                if (querier == null){
                    return;
                }
                //获取实现
                DataSourceLookupStrategy dataSourceLookupStrategy = DataSourceLookupStrategyFactory.getByHolderType(HolderTypeEnum.GLOBAL.getHolderType());
                //查询当前数据源
                String currentDbSource = querier.queryCurrentDbSource();
                if (currentDbSource == null || currentDbSource.length() < 1){
                    return;
                }
                //切换
                if (dataSourceLookupStrategy instanceof GlobalDataSourceLookupStrategy){
                    GlobalDataSourceLookupStrategy strategy = (GlobalDataSourceLookupStrategy) dataSourceLookupStrategy;
                    String oldCurrent = strategy.getDataSourceType();
                    if (oldCurrent == null || !oldCurrent.equals(currentDbSource)){
                        LOGGER.info("数据源切换，由数据源：{}切换到数据源：{}", oldCurrent, currentDbSource);
                        strategy.changeDataSource(currentDbSource);
                    }
                }

            }catch (Throwable e){
                LOGGER.error("ChangeDataSourceTask error", e);
            }
        }
    }

}
