package cn.com.kun.component.graydeploy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "graydeploy")
public class GrayDeployProperties {

    /**
     * 全局开关,默认为关闭
     * 设置为false,禁用MemoryCache功能
     */
    private boolean enabled;

    private MysqlProp mysql;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public MysqlProp getMysql() {
        return mysql;
    }

    public void setMysql(MysqlProp mysql) {
        this.mysql = mysql;
    }
}
