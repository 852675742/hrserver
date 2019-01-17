package org.sang.mq;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rick on 2018/10/18.
 *
 * @ Description：消息监听接收
 */
@Component
public class MQReceiver {

    private static final Logger logger = Logger.getLogger(MQReceiver.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @RabbitListener(queues = MQConfig.DIRECT_QUEUE_NAME)
    public void receive(Channel channel,Message message){
        String messageContent = new String(message.getBody());
        String msgId = new String(message.getMessageProperties().getCorrelationId());//target没有字段CorrelationIdStrng
        logger.info("消息唯一id:" + msgId);
        System.out.println(sdf.format(new Date()) + "收到消息:" + messageContent);
        try {
            //实际业务会先登记一个消息表，并把消息id作为唯一键防止重复消费（报错了考虑回滚）
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            //没有成功执行以下语句的消息会被重新丢到队列里面消费
            //消息的标识，false只确认当前一个消息收到，true确认所有消费者获得的消息，因为有些消息可能有多个消费者
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            System.out.println(sdf.format(new Date()) + "消息消费成功");
        } catch (IOException e) {
            e.printStackTrace();
            //丢弃这条消息
            //ack返回false，并重新回到队列
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            System.err.println(new Date() + "消息消费失败");
        }
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_NAME1)
    public void receiveTopic1(Channel channel,Message message){
        String messageContent = new String(message.getBody());
        logger.info("topic queue1 receive:" + messageContent);
        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            System.out.println(new Date() + "消息接收成功");
        } catch (IOException e) {
            e.printStackTrace();
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            System.out.println(new Date() + "消息接收失败");
        }
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_NAME2)
    public void receiveTopic2(Channel channel,Message message){
        String messageContent = new String(message.getBody());
        logger.info("topic queue2 receive:" + messageContent);
        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            System.out.println(new Date() + "消息接收成功");
        } catch (IOException e) {
            e.printStackTrace();
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            System.out.println(new Date() + "消息接收失败");
        }
    }


}
