package org.sang.assit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.sang.common.annotation.QCache;
import org.sang.util.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;

/**
 * @author yq
 * @date 2018/12/20 15:40
 * @description 注解支持
 * @since V1.0.0
 */
public abstract class QCacheSupport {

    private RedisTemplate<String,Object> redisTemplate;

    public QCacheSupport(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Object support(ProceedingJoinPoint pjp) throws Throwable{
        Class<?> classTarget = pjp.getTarget().getClass();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Class<?>[] par = signature.getParameterTypes();
        String methodName = pjp.getSignature().getName();
        Method objMethod = classTarget.getMethod(methodName, par);
        //QCache qCache = objMethod.getAnnotation(QCache.class);//此方法别名不生效
        QCache qCache =AnnotationUtils.getAnnotation(objMethod,QCache.class);
        Object result = null;
        QCache.RedisType redisType = qCache.type();
        IRedisService iRedisService = new IRedisService() {
            @Override
            public Object originalData() throws Throwable {
                return pjp.proceed();
            }

            @Override
            public RedisTemplate<String, Object> redisTemplate() {
                return redisTemplate;
            }
        };
        String key = qCache.key() ;
        //key优先级最高，相当于从redis中直接获取
        //如果没有设置key但是设置了expression则从expression解析出作为默认的key
        if(StringUtils.isEmpty(key)){
                String expression = qCache.expression();
                if (StringUtils.isNotEmpty(expression)) {
                    Object dynamicValue = CustomSpringExpressionLanguageParser.
                            getDynamicValue(signature.getParameterNames(), pjp.getArgs(),expression);
                    if(dynamicValue != null){
                        key = dynamicValue.toString();
                    }
                }
        }
        if(StringUtils.isNotEmpty(key)){
            switch (redisType){
                case STRING:
                    result = iRedisService.getValue(key);
                    break;
                case HASH:
                    result = iRedisService.getValue(qCache.hash(),key);
                    break;
                default:
                    break;
            }
        }
        return result;
    }

}
