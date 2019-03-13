package org.sang.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by Rick on 2019/3/12.
 *
 * @ Description：${description}
 */
@PropertySource("classpath:redis.properties")
@Component
public class RedisProperties implements Serializable{

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

    @Value("${redis.timeout}")
    private Integer timeout;

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(Integer maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public Integer getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public Integer getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(Integer numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public String getSentinelNodes() {
        return sentinelNodes;
    }

    public void setSentinelNodes(String sentinelNodes) {
        this.sentinelNodes = sentinelNodes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMasterHost() {
        return masterHost;
    }

    public void setMasterHost(String masterHost) {
        this.masterHost = masterHost;
    }

    public Integer getMasterPort() {
        return masterPort;
    }

    public void setMasterPort(Integer masterPort) {
        this.masterPort = masterPort;
    }


    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
