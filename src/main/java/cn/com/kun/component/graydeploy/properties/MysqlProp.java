package cn.com.kun.component.graydeploy.properties;

/**
 * MySQL灰度相关配置
 *
 * author:xuyaokun_kzx
 * date:2024/3/29
 * desc:
*/
public class MysqlProp {

    /**
     * 业务表前缀
     * 假如不指定，则是全部拦截
     */
    private String tablePrefix;

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 跳过灰度的表集合
     */
    private String skipTables;

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSkipTables() {
        return skipTables;
    }

    public void setSkipTables(String skipTables) {
        this.skipTables = skipTables;
    }
}
