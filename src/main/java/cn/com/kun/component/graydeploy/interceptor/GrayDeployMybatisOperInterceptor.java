package cn.com.kun.component.graydeploy.interceptor;

import cn.com.kun.component.graydeploy.properties.GrayDeployProperties;
import cn.com.kun.component.graydeploy.properties.MysqlProp;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mybatis操作拦截器
 *
 * author:xuyaokun_kzx
 * date:2024/3/29
 * desc:
*/
@ConditionalOnProperty(prefix = "graydeploy.mysql", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class GrayDeployMybatisOperInterceptor implements Interceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(GrayDeployMybatisOperInterceptor.class);

    private Pattern pattern = Pattern.compile("(from|into|update)[\\s]{1,}(\\w{1,})", Pattern.CASE_INSENSITIVE);

    private final static String GRAY_TABLE_SUFFIX = "_gray";

    @Autowired
    private GrayDeployProperties grayDeployProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        // 获取自定义注解判断是否进行分表操作
        String id = mappedStatement.getId();

        //可以通过Class对象获取注解
//        String className = id.substring(0, id.lastIndexOf("."));
//        Class<?> clazz = Class.forName(className);
//        clazz.getAnnotation()

        // 获取SQL
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        // 替换SQL表名
        Matcher matcher = pattern.matcher(sql);
        //from tablename or into tablename or update tablename
        String targetString = null;
        if (matcher.find()) {
            targetString = matcher.group().trim();
        }
        assert null != targetString;
        String tablename = getTableName(targetString);

        //确定表名是业务表再进行拦截
        String tableNamePrefix = getTableNamePrefix();

        String replaceSql = sql;
        if (tablename != null && tablename.toLowerCase().startsWith(tableNamePrefix.toLowerCase()) && !isSkipTables(tablename)){
            replaceSql  = sql.replaceAll(targetString, targetString + GRAY_TABLE_SUFFIX);
        }
        // 通过反射修改SQL语句
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, replaceSql);
        field.setAccessible(false);

        return invocation.proceed();
    }

    /**
     * 是否为跳过灰度的表
     *
     * @param tablename
     * @return
     */
    private boolean isSkipTables(String tablename) {

        MysqlProp mysqlProp = grayDeployProperties.getMysql();
        if (mysqlProp != null && mysqlProp.getSkipTables() != null && mysqlProp.getSkipTables().length() > 0){
            String[] tables = mysqlProp.getSkipTables().split(",");
            for (String table : tables){
                if (tablename.toLowerCase().equals(table)){
                    return true;
                }
            }
        }

        return false;
    }

    private String getTableName(String targetString) {

        int index = targetString.indexOf(" ");
        return targetString.substring(index, targetString.length()).trim();
    }

    /**
     * 获取业务表前缀
     *
     * @return
     */
    private String getTableNamePrefix() {

        MysqlProp mysqlProp = grayDeployProperties.getMysql();
        return mysqlProp != null ? mysqlProp.getTablePrefix() : "";
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }


}
