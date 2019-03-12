package org.sang.common.annotation;

import org.sang.config.RedisSelector;
import org.sang.config.RedisSentinelConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by Rick on 2019/3/12.
 *
 * @ Description：开启redis开关
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisSelector.class)
public @interface EnableRedis {

    boolean enable() default true;

}
