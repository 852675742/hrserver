package org.sang.config;

import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Rick on 2019/1/24.
 *
 * @ Description：zk配置
 */
@Component
@ConfigurationProperties(prefix = "zookeeper")
@Data
public class ZkConfig {

    private String server;

    private int sessionTimeoutMs;

    private int connectionTimeoutMs;

    private int maxRetries;

    private int baseSleepTimeMs;

    @Bean(name="curatorClient")
    private CuratorFramework curatorClient() {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(server).sessionTimeoutMs(sessionTimeoutMs).connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(new ExponentialBackoffRetry(baseSleepTimeMs,maxRetries)).build();
        client.start();
        return client;
    }
}
