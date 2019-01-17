package org.sang.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.sang.assit.QCacheSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/*
*
 * Created by Rick on 2018/12/25.
 *
 * @ Description：切面配置
*/


@Aspect
@Component
@Slf4j
@Order(2)
public class QCacheAop {

    public static final String EDP = "execution(* org.sang.service..*(..)) && @annotation(org.sang.common.annotation.QCache)";

    private QCacheSupport qCacheSupport;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;//此处不能用AutoWired

    //初始化QCacheSupport
    @PostConstruct
    public void init(){
        qCacheSupport = new QCacheSupport(redisTemplate){

        };
    }

    @Around(EDP)
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("进入环绕通知...");
        return qCacheSupport.support(joinPoint);
    }

}
