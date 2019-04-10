package org.sang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Rick on 2019/3/13.
 *
 * @ Description：限流
 */
@Component
public class RedisLimitTool {

    private static RedisTemplate redisTemplateSingle;

    @Autowired
    public void init(RedisTemplate redisTemplate) {
        this.redisTemplateSingle = redisTemplate;
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

    private static final String LUA_LIMIT_SCRIPT2 = "--- 资源唯一标识\n" +
            "local key = KEYS[1]\n" +
            "--- 时间窗最大并发数\n" +
            "local limit = tonumber(ARGV[1])\n" +
            "redis.log(redis.LOG_DEBUG, ARGV[1])\n" +
            "--- 时间窗\n" +
            "local window = tonumber(ARGV[2])\n" +
            "redis.log(redis.LOG_DEBUG, ARGV[2])\n" +
            "--- 时间窗内当前并发数\n" +
            "local current = tonumber(redis.call('get', key) or 0)\n" +
            "if current + 1 > limit then\n" +
            "return 0\n" +
            "else\n" +
            "redis.call(\"INCRBY\", key,1)\n" +
            "if window > -1 then\n" +
            "redis.call(\"expire\", key,window)\n" +
            "end\n" +
            "return 1\n" +
            "end";

    private static final Long SUCCESS_CODE = 1L;

    public static Boolean limit(String keyPrefix, String limit, String timeOut){
        //String key = keyPrefix + ":" + System.currentTimeMillis() / 1000;//同一时刻的并发
        String key = keyPrefix;
        DefaultRedisScript<Long> redisScript2 = new DefaultRedisScript<>(LUA_LIMIT_SCRIPT2, Long.class);

        //参数必须加序列化，否则会报错;返回值根据实际情况加序列化
        Long res =(Long) redisTemplateSingle.execute(redisScript2, new StringRedisSerializer(),null , Collections.singletonList(key),limit,timeOut);

        System.out.println(new Date() + ":" + res);
        return SUCCESS_CODE.equals(res);
    }


}
