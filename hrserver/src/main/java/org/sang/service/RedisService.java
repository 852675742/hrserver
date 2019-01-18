package org.sang.service;

import org.sang.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
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

    public String put3() {
        System.out.println("修改前name:" + StringUtils.null2EmptyWithTrim(redisTemplate.opsForValue().get("name")));
        Object txResults = redisTemplate.execute(new SessionCallback<Object>() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                //以下多个操作将在同一个事物中
                operations.delete("name");
                operations.opsForValue().set("name", "rick123456789");
                //This will contain the results of all ops in the transaction
                return operations.exec();
            }
        });
        String name = org.sang.util.StringUtils.null2EmptyWithTrim(redisTemplate.opsForValue().get("name"));
        System.out.println("修改后name:" + name);
        return null;
    }

}
