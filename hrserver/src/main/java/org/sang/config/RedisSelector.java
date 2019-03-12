package org.sang.config;

import org.sang.common.annotation.EnableRedis;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Objects;

/**
 * Created by Rick on 2019/3/12.
 *
 * @ Description：导入选择器
 */
public class RedisSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        boolean enabled = Boolean.valueOf(
                Objects.requireNonNull(
                        annotationMetadata.getAnnotationAttributes(EnableRedis.class.getName()))
                        .get("enable").toString());
        return enabled ? new String[ ] {RedisSentinelConfig.class.getName()} : new String[] {};//此处不能返回空
    }
}
