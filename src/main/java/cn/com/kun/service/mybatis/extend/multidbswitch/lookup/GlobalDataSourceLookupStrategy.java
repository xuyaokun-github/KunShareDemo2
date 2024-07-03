package cn.com.kun.service.mybatis.extend.multidbswitch.lookup;

import java.util.HashMap;
import java.util.Map;

/**
 * 选中的数据源，放在集合中。应对全局切换场景
 *
 * author:xuyaokun_kzx
 * date:2024/7/2
 * desc:
*/
public class GlobalDataSourceLookupStrategy implements DataSourceLookupStrategy {

    private Map<String, String> currentMap = new HashMap<>();

    private String CURRENT_DB = "currentDb";

    public String getDataSourceType() {
        return currentMap.get(CURRENT_DB);
    }

    /**
     * 切换数据源
     *
     * @param dataSourceName
     * @return
     */
    public String changeDataSource(String dataSourceName) {
        return currentMap.put(CURRENT_DB, dataSourceName);
    }


}
