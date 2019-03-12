package com.miracle.autoconfigure;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = HashidsProperties.class)
@ConditionalOnProperty(prefix = "hashids", value = "enable", matchIfMissing = true)
public class HashidsConfiguration {

    @Autowired
    private HashidsProperties hashidsProperties;

    //测试
    @Bean
    @ConditionalOnMissingBean(HelloService.class)
    public HelloService helloService() {
        HelloService helloService = new HelloService(hashidsProperties);
        return helloService;
    }

}
