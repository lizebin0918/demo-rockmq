package com.lzb;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * <br/>
 * Created on : 2021-04-15 08:18
 * @author lizebin
 */
public class Main {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.56.100:9876");
        producer.setSendMsgTimeout(60000);
        producer.start();

        //Message msg = new Message("MyTopic", "hello world".getBytes());
        //producer.send(msg);

    }

}
