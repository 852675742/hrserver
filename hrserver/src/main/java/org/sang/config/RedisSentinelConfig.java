package org.sang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rick on 2019/1/17.
 *
 * @ Description：redis哨兵模式测试
 */
//@Configuration
//@PropertySource("classpath:redis.properties")
public class RedisSentinelConfig {

    /*
    @Value("${redis.maxIdle}")
    private Integer maxIdle;

    @Value("${redis.maxTotal}")
    private Integer maxTotal;

    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;

    @Value("${redis.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;

    @Value("${redis.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;

    @Value("${redis.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${redis.testWhileIdle}")
    private boolean testWhileIdle;


    @Value("${redis.sentinel.nodes}")
    private String sentinelNodes;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.hostName}")
    private String masterHost;

    @Value("${redis.port}")
    private Integer masterPort;
    */

    @Autowired
    private RedisProperties redisProperties;

    /**
     * JedisPoolConfig 连接池
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(redisProperties.getMinEvictableIdleTimeMillis());
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(redisProperties.getNumTestsPerEvictionRun());
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(redisProperties.getTimeBetweenEvictionRunsMillis());
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(redisProperties.isTestOnBorrow());
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(redisProperties.isTestWhileIdle());

        return jedisPoolConfig;
    }
    /**
     * 配置redis的哨兵
     * @return RedisSentinelConfiguration
     * @autor lpl
     * @date 2017年12月21日
     * @throws
     */
    @Bean(value="sentinelConfiguration")
    public RedisSentinelConfiguration sentinelConfiguration(){
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
        //配置matser的名称
        RedisNode masterRedisNode = new RedisNode(redisProperties.getMasterHost(), redisProperties.getMasterPort());
        masterRedisNode.setName("mymaster");
        redisSentinelConfiguration.master(masterRedisNode);

        //配置redis的哨兵sentinel
        Set<RedisNode> redisNodeSet = new HashSet<>();
        for(String redisNode : redisProperties.getSentinelNodes().split(";")) {
            String[] nodeInfo = redisNode.split(":");
            RedisNode senRedisNode = new RedisNode(nodeInfo[0],Integer.parseInt(nodeInfo[1]));
            redisNodeSet.add(senRedisNode);
        }
        redisSentinelConfiguration.setSentinels(redisNodeSet);
        return redisSentinelConfiguration;
    }
    /**
     * 配置工厂
     * @param jedisPoolConfig
     * @return
     */
    @Bean(name="jedisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory(@Qualifier(value="jedisPoolConfig") JedisPoolConfig jedisPoolConfig, @Qualifier(value="sentinelConfiguration") RedisSentinelConfiguration sentinelConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(sentinelConfig,jedisPoolConfig);
        jedisConnectionFactory.setPassword(redisProperties.getPassword());
        return jedisConnectionFactory;
    }

    /**
     *
     * @author Fire Monkey
     * @date 下午7:20
     * @return redis.clients.jedis.JedisSentinelPool
     * 生成JedisSentinelPool并且放入Spring容器
     *
     */
    @Bean(value = "jedisSentinelPool")
    public JedisSentinelPool jedisSentinelPool(@Qualifier(value = "jedisPoolConfig") JedisPoolConfig jedisPoolConfig){

        Set<String>  nodeSet = new HashSet<>();
        //获取到节点信息
        String nodeString = redisProperties.getSentinelNodes();
        //判断字符串是否为空
        if(nodeString == null || "".equals(nodeString)){
            throw new RuntimeException("RedisSentinelConfiguration initialize error nodeString is null");
        }
        String[] nodeArray = nodeString.split(";");
        //判断是否为空
        if(nodeArray == null || nodeArray.length == 0){
            throw new RuntimeException("RedisSentinelConfiguration initialize error nodeArray is null");
        }
        //循环注入至Set中
        for(String node : nodeArray){
            nodeSet.add(node);
        }
        //创建连接池对象
        JedisSentinelPool jedisPool = new JedisSentinelPool("mymaster" ,nodeSet ,jedisPoolConfig ,redisProperties.getTimeout(), redisProperties.getPassword());
        return jedisPool;
    }

    /**
     * 实例化 RedisTemplate 对象(带事物)
     *
     * @return
     */
    @Bean(name="redisTransactionTemplate")
    public RedisTemplate<String, Object> functionDomainRedisTemplate(@Qualifier(value="jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 不带事物
     * @param jedisConnectionFactory
     * @return
     */
    @Bean(name="redisemplate")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier(value="jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(false);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 封装RedisTemplate
     * @Title: redisUtil
     * @return RedisUtil
     * @autor lpl
     * @date 2017年12月21日
     * @throws
     */
    @Bean(name = "redisUtil")
    public RedisUtil redisUtil(@Qualifier(value="redisTemplate") RedisTemplate redisTemplate) {
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }

}
