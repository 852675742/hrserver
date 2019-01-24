package org.sang.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.sang.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Rick on 2019/1/23.
 *
 * @ Description：启动监听器
 */
@Component
@Slf4j
public class WebListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CuratorFramework curatorClient;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    Environment environment;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("服务启动成功");
        RequestMappingHandlerMapping mapping = contextRefreshedEvent.getApplicationContext().getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap =  mapping.getHandlerMethods();
        handlerMethodMap.forEach((k,v)->{
            log.debug("k:{},v:{}",k,v);
        });
        try {
            this.register();
        } catch (Exception e) {
            log.error("zk注册失败",e);
        }
    }

    /**
     * zk注册服务
     */
    public void register() throws Exception {
        final String rootPath = "/services";
        if (!Optional.ofNullable(curatorClient.checkExists().forPath(rootPath + "/" + applicationName)).isPresent()) {
            curatorClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(rootPath + "/" + applicationName);//持久节点
        }
        //创建子节点
        final String servicePath = rootPath + "/" + applicationName + "/" + applicationName;

        JSONObject nodeContent = new JSONObject(true)
                .fluentPut("ip", StringUtils.isNotEmpty(environment.getProperty("server.address")) ? environment.getProperty("server.address") : InetAddress.getLocalHost().getHostAddress())
                .fluentPut("port", environment.getProperty("server.port"))
                .fluentPut("name",applicationName)
                .fluentPut("num",0)
                .fluentPut("status","wait");
        curatorClient.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(servicePath,nodeContent.toJSONString().getBytes());//临时有序节点
    }
}
