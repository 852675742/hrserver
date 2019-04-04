package org.sang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rick on 2019/3/13.
 *
 * @ Description：限流
 */
@Component
public class RedisLimitTool {

    private static RedisTemplate redisTemplate;

    @Autowired
    public void init(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String LUA_LIMIT_SCRIPT = "local key = KEYS[1]\n" +
            "local limit = ARGV[1]\n" +
            "local current = tonumber(redis.call('get', key) or \"0\")\n" +
            "if current + 1 > tonumber(limit) then\n" +
            "   return 0\n" +
            "else\n" +
            "   redis.call(\"INCRBY\", key,\"1\")\n" +
            "   redis.call(\"expire\", key,\"2\")\n" +
            "   return 1\n" +
            "end";

    private static final Long SUCCESS_CODE = 1L;

    public static Boolean limit(String keyPrefix, String limit){
        String key = keyPrefix + ":" + System.currentTimeMillis() / 1000;
        DefaultRedisScript<Long> redisScript1 = new DefaultRedisScript<>(LUA_LIMIT_SCRIPT, Long.class);

        Long res =(Long) redisTemplate.execute(redisScript1, Collections.singletonList(key),Collections.singletonList(limit));

        System.out.println("xxyyzz:" + res);
        return SUCCESS_CODE.equals(res);
    }


}
