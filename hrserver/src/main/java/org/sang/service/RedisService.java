package org.sang.service;

import org.sang.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    
    private RedisTemplate redisTemplate;//不带事物

    private RedisTemplate redisTransactionTemplate;//带事物

    @Autowired
    public RedisService(RedisTemplate redisTemplate,RedisTemplate redisTransactionTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTransactionTemplate = redisTransactionTemplate;
    }

    @Transactional
    public String put1() {
        System.out.println("修改前name:" + StringUtils.null2EmptyWithTrim(redisTemplate.opsForValue().get("name")));
        redisTemplate.opsForValue().set("name", "rick");
        String name = StringUtils.null2EmptyWithTrim(redisTemplate.opsForValue().get("name"));
        System.out.println("修改后name:" + name);
        return null;
    }

    @Transactional
    public String put2() {
        System.out.println("修改前name:" + StringUtils.null2EmptyWithTrim(redisTransactionTemplate.opsForValue().get("name")));
        redisTransactionTemplate.opsForValue().set("name", "rick987");
        String name = org.sang.util.StringUtils.null2EmptyWithTrim(redisTransactionTemplate.opsForValue().get("name"));
        System.out.println("修改后name:" + name);
        return null;
    }

}
