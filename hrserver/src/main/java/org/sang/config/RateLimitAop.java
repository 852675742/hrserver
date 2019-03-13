package org.sang.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.sang.common.annotation.RedisLimiter;
import org.springframework.stereotype.Component;

/**
 * Created by Rick on 2019/3/13.
 *
 * @ Description：${description}
 */
@Aspect
@Component
@Slf4j
public class RateLimitAop {

    @Around("execution(* org.sang.controller..*(..)) && @annotation(redisLimiter)")
    public Object redisLimiter(ProceedingJoinPoint proceedingJoinPoint, RedisLimiter redisLimiter) throws Throwable {
            if (RedisLimitTool.limit(redisLimiter.keyPrefix(), redisLimiter.limit())){
                return proceedingJoinPoint.proceed();
            }else {
                log.error("限流：" + redisLimiter.keyPrefix());
                return "current server is busy,please try it later";
            }
    }


}
