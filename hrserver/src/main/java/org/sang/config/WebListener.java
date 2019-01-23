package org.sang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContextListener;
import java.util.Map;

/**
 * Created by Rick on 2019/1/23.
 *
 * @ Description：启动监听器
 */
@Component
@Slf4j
public class WebListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("服务启动成功");
        RequestMappingHandlerMapping mapping = contextRefreshedEvent.getApplicationContext().getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap =  mapping.getHandlerMethods();
        handlerMethodMap.forEach((k,v)->{
            log.debug("k:{},v:{}",k,v);
        });
    }
}
