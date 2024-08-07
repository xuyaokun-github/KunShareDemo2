package cn.com.kun.config.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 主数据源（假如同时开启多数据源时，它就是主数据源）
 *
 * author:xuyaokun_kzx
 * desc:
*/
@Configuration
@ConditionalOnProperty(prefix = "kunsharedemo.quartz.datasource", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
public class QuartzDbDataSourceConfig {


    @Value("${spring.datasource.quartzDataSource.url}")
    private String dbUrl;

    @Value("${spring.datasource.quartzDataSource.username}")
    private String username;

    @Value("${spring.datasource.quartzDataSource.password}")
    private String password;

    @Value("${spring.datasource.quartzDataSource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.quartzDataSource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.quartzDataSource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.quartzDataSource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.quartzDataSource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.quartzDataSource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.quartzDataSource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.quartzDataSource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.quartzDataSource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.quartzDataSource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.quartzDataSource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.quartzDataSource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.quartzDataSource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.quartzDataSource.filters}")
    private String filters;

    @Value("${spring.datasource.quartzDataSource.connectionProperties}")
    private String connectionProperties;

    @Value("${spring.datasource.quartzDataSource.passwordCallbackClassName}")
    private String passwordCallbackClassName;

//    @Primary
    @Bean(name="quartzDataSource")
    public DataSource quartzDataSource() throws Exception {

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

