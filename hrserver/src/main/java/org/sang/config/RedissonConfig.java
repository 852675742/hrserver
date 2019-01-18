package org.sang.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.sang.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson 配置类
 * Created on 2018/6/19
 */
@Configuration
public class RedissonConfig {

    @Value("${redis.sentinel.nodes}")
    private String sentinelNodes;

    @Value("${redis.master.name}")
    private String masterName;

    @Value("${redis.password}")
    private String password;

    @Bean
    public RedissonClient getRedisson(){
        //哨兵模式
        Config config = new Config();
        SentinelServersConfig serverConfig = config.useSentinelServers().addSentinelAddress("redis://" + sentinelNodes.replace(";",""))
                .setMasterName(masterName)
                .setTimeout(3000)
                .setMasterConnectionPoolSize(250)
                .setSlaveConnectionPoolSize(250);

        if(StringUtils.isNotEmpty(password)) {
            serverConfig.setPassword(password);
        }
        return Redisson.create(config);
    }

    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     * @return
             */
    @Bean
    DistributedLocker distributedLocker(RedissonClient redissonClient) {
        DistributedLocker locker = new RedissonDistributedLocker(redissonClient);
        RedissLockUtil.setLocker(locker);
        return locker;
    }

}