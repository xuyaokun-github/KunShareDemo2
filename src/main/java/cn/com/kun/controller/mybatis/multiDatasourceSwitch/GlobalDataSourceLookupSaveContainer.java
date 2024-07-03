package cn.com.kun.controller.mybatis.multiDatasourceSwitch;

import java.util.HashMap;
import java.util.Map;

public class GlobalDataSourceLookupSaveContainer {

    private static Map<String, String> currentMap = new HashMap<>();

    private static String CURRENT_DB = "currentDb";

    public static void changeDataSource(String datasource) {
        currentMap.put(CURRENT_DB, datasource);
    }


    public static String getCurrentDb() {

        return currentMap.get(CURRENT_DB);
    }


}
