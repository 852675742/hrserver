package org.sang.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Rick on 2019/3/6.
 *
 * @ Description：${description}
 */
public interface TestApi {

    //可以定义到一个独立的模块供其他服务端调用
    @GetMapping(value="testFegin")
    public String testFegin(@RequestParam(value="id") String id);

}
