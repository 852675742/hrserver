package org.sang.common.annotation;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义缓存注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface QCache {

    /**
     * 缓存hash
     * @return hash
     */
    String hash() default "";

    /**
     * 缓存 hash 内部 key (最高优先级)
     * @return key
     */
    String key() default "";


    /**
     * 参数表达式 (末位)
     * 例: method(A a) , 已a为key 则,使用 #a
     * @return SpringEL
     */
    String expression() default "";


    /**
     * redis数据类型
     * @return RedisType
     */
    RedisType type() default RedisType.HASH;

    /**
     * redis数据类型
     */
    enum RedisType{
        /**
         * 字符串
         */
        STRING,
        /**
         * 哈希
         */
        HASH
    }
}
