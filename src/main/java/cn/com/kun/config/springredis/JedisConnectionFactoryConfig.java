package cn.com.kun.config.springredis;

import cn.com.kun.common.utils.DesedeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Jedis连接池配置
 *
 * author:xuyaokun_kzx
 * date:2022/11/15
 * desc:
*/
//@DependsOn({"desedeUtils"}) //保证desedeUtils先加载
@Configuration
public class JedisConnectionFactoryConfig {

    @Value("${spring.redis.cluster.nodes:}")
    private String host;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    /* redis集群模式 用下面的代码 */
    /* 假如要启用JedisConnectionFactory，禁用LettuceConnectionConfiguration，需要加如下代码  start */

    @ConditionalOnProperty(prefix = "kunsharedemo.rediscommon.cluster", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
    @Bean
    public RedisClusterConfiguration redisClusterConfiguration() {

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        String[] hosts = host.split(",");
        Set<RedisNode> nodeList = new HashSet<RedisNode>();
        for (String hostAndPort : hosts) {
            String[] hostOrPort = hostAndPort.split(":");
            nodeList.add(new RedisNode(hostOrPort[0], Integer.parseInt(hostOrPort[1])));
        }
        redisClusterConfiguration.setClusterNodes(nodeList);
//		redisClusterConfiguration.setMaxRedirects();
        return redisClusterConfiguration;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(this.maxIdle);
        poolConfig.setMinIdle(this.minIdle);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        return poolConfig;
    }

    @ConditionalOnProperty(prefix = "kunsharedemo.rediscommon.cluster", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
    @Bean("myJedisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory(RedisClusterConfiguration redisClusterConfiguration,
                                                         JedisPoolConfig jedisPoolConfig) {

        //集群模式
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, jedisPoolConfig);
        jedisConnectionFactory.setPassword(password);
        return jedisConnectionFactory;
    }

    @ConditionalOnProperty(prefix = "kunsharedemo.rediscommon.cluster", value = {"enabled"}, havingValue = "false", matchIfMissing = true)
    @Bean("mySingleJedisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory2(JedisPoolConfig jedisPoolConfig) {

        //单节点模式
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        jedisConnectionFactory.setPassword(password);

        /**
         * 假如DesedeUtils bean未加载，这里会报空指针，因为单例尚未初始化
         * 两种解决办法：
         * 1.加@DependsOn({"desedeUtils"})
         * 假如多个位置都用到该加解密工具类，都要加注解，这种代码不够优雅，并且看出了依赖关系
         * 2.
         */
        String sourcePassword = DesedeUtils.getInstance().decryptStringBase64("5vrTTz2MLfE=");

        return jedisConnectionFactory;
    }

    /* 假如要启用JedisConnectionFactory，禁用LettuceConnectionConfiguration  end */
}
