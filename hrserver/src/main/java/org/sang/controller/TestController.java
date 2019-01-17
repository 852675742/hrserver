package org.sang.controller;

import org.sang.mq.MQConfig;
import org.sang.mq.MQSender;
import org.sang.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Rick on 2018/10/18.
 *
 * @ Description：${description}
 */
@RestController
@RequestMapping(value="test")
public class TestController {

    @Autowired
    private MQSender mqSender;

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("/mq")
    public String mq(){
        mqSender.send(MQConfig.DIRECT_QUEUE_NAME,"{\n" +
                "\t\"scene\": \"bar_code\",\n" +
                "\t\"auth_code\": \"10123456789\",\n" +
                "\t\"out_trade_no\": \"tradepay1539827578433171121\",\n" +
                "\t\"seller_id\": \"\",\n" +
                "\t\"total_amount\": \"0.01\",\n" +
                "\t\"undiscountable_amount\": \"0.0\",\n" +
                "\t\"subject\": \"xxx品牌xxx门店当面付消费\",\n" +
                "\t\"body\": \"购买商品3件共20.00元\",\n" +
                "\t\"goods_detail\": [{\n" +
                "\t\t\"goods_id\": \"goods_id001\",\n" +
                "\t\t\"goods_name\": \"xxx面包\",\n" +
                "\t\t\"quantity\": 1,\n" +
                "\t\t\"price\": \"10\"\n" +
                "\t}, {\n" +
                "\t\t\"goods_id\": \"goods_id002\",\n" +
                "\t\t\"goods_name\": \"xxx牙刷\",\n" +
                "\t\t\"quantity\": 2,\n" +
                "\t\t\"price\": \"5\"\n" +
                "\t}],\n" +
                "\t\"operator_id\": \"test_operator_id\",\n" +
                "\t\"store_id\": \"test_store_id\",\n" +
                "\t\"extend_params\": {\n" +
                "\t\t\"sys_service_provider_id\": \"2088100200300400500\"\n" +
                "\t},\n" +
                "\t\"timeout_express\": \"5m\"\n" +
                "}");
        return "success";
    }

    @RequestMapping("/mq2")
    public String mq2(){
        mqSender.sendTopic2("测试消息");
        return "success";
    }

    @RequestMapping("/mq3")
    public String mq3(){
        mqSender.sendTopic3("测试消息");
        return "success";
    }

    @RequestMapping("/clear")
    public String clear(){
        departmentService.remove(2L);
        return "success";
    }

}