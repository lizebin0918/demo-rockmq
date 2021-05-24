package com.lzb.demo.ordermessage;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 消费者<br/>
 * Created on : 2021-04-18 15:10
 *
 * @author lizebin
 */
public class Consumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ordermessage-" + "consumer");
        consumer.setNamesrvAddr("192.168.56.100:9876");

        //一个consumer只能subscribe一个topic，第二个参数表示过滤器
        consumer.subscribe("ordermessage", "*");

        //默认情况下，只能被一个消费者消费消息，P2P模式
        consumer.registerMessageListener(new MessageListenerOrderly() {
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                for (MessageExt message : list) {
                    System.out.println("message : " + JSON.toJSONString(new String(message.getBody())));
                }
                //ack
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();

        System.out.println("consumer start");
    }
}
