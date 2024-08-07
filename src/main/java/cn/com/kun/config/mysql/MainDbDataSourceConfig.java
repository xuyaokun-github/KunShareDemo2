package cn.com.kun.config.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 主数据源（假如同时开启多数据源时，它就是主数据源）
 *
 * author:xuyaokun_kzx
 * desc:
*/
@Configuration
@ConditionalOnProperty(prefix = "kunsharedemo.maindb.datasource", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
public class MainDbDataSourceConfig {
    
    @Value("${spring.datasource.mainDataSource.url}")
    private String dbUrl;

    @Value("${spring.datasource.mainDataSource.username}")
    private String username;

    @Value("${spring.datasource.mainDataSource.password}")
    private String password;

    @Value("${spring.datasource.mainDataSource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.mainDataSource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.mainDataSource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.mainDataSource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.mainDataSource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.mainDataSource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.mainDataSource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.mainDataSource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.mainDataSource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.mainDataSource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.mainDataSource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.mainDataSource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.mainDataSource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.mainDataSource.filters}")
    private String filters;

    @Value("${spring.datasource.mainDataSource.connectionProperties}")
    private String connectionProperties;

    @Value("${spring.datasource.mainDataSource.passwordCallbackClassName}")
    private String passwordCallbackClassName;

    @Primary
    @Bean(name="mainDataSource")
    public DataSource mainDataSource() throws Exception {

        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        datasource.setConnectionProperties(connectionProperties);
        datasource.setPasswordCallbackClassName(passwordCallbackClassName);

        return datasource;
    }
}

