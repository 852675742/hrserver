package org.sang.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class MQSender implements RabbitTemplate.ReturnCallback{

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public void send(String queueName,Object messageObj){
        /*
        amqpTemplate.convertAndSend(queueName,message);
        */
        rabbitTemplate.setReturnCallback(this);
        //用来确认是否送达消息队列
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) ->{
            //实际业务可能需要根据发送情况更新日志表的状态
            if (!ack) {
                //失败则进行具体的后续操作:重试 或者补偿等手段等？一般会借助定时器来处理，比如重试次数等
                System.out.println(sdf.format(new Date()) + "消息发送失败" + cause + correlationData.toString());
                //表明消息一定成功发送到消费者方了但不保证被消费者成功消费
            } else {
                //可以参考文章https://www.imooc.com/article/49814
                System.out.println(sdf.format(new Date()) + "消息发送成功" + correlationData.toString());
            }
        });
        System.out.println(new Date() + "准备发送消息:" + messageObj);

        //实际业务可能需要先记录起来
        String msgId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(msgId);
        Message message = MessageBuilder.withBody(messageObj.toString().getBytes()).setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setCorrelationIdString(msgId).setCorrelationId(msgId.getBytes()).build();
        rabbitTemplate.convertAndSend(queueName, message,correlationData);
    }

    public void sendTopic2(Object message){
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE_NAME,"topic.key1",message + "---1");
        System.out.println("send topic msg:" + message + "---1");
    }

    public void sendTopic3(Object message){
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE_NAME,"topic.key2",message + "---2");
        System.out.println("send topic msg:" + message + "---2");
    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println("sender return success:" + message.toString()+"==="+i+"==="+s1+"==="+s2);
    }
}