package cn.com.kun.service.mybatis.extend.multidbswitch.lookup;

/**
 * 数据源筛选策略
 *
 * author:xuyaokun_kzx
 * date:2024/7/2
 * desc:
*/
public interface DataSourceLookupStrategy {

    String getDataSourceType();

}
