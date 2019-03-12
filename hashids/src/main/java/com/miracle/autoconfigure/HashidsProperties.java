package com.miracle.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Rick on 2019/3/12.
 *
 */
@Data
@ConfigurationProperties(prefix = "hashids")
public class HashidsProperties {

    private String salt;


}
