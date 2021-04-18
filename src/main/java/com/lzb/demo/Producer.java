package com.lzb.demo;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <br/>
 * Created on : 2021-04-15 08:18
 *
 * @author lizebin
 */
public class Producer {

    private static final DefaultMQProducer PRODUCER = new DefaultMQProducer("producerGroup");

    static {
        //设置nameserver地址
        PRODUCER.setNamesrvAddr("192.168.56.100:9876");
        PRODUCER.setSendMsgTimeout(60000);
        try {
            PRODUCER.start();
        } catch (Exception e) {
            //ignore e
        }
    }

    public static void main(String[] args) throws Exception {
        String topic = "MyTopic";
        SendResult sendResult = sendSingle(topic, ("hello world-" + RandomStringUtils.randomAlphabetic(5)));

        System.out.println(JSON.toJSONString(sendResult));

        //异步消息，不阻塞，采用事件监听接受broker回调
        PRODUCER.send(new Message("MyTopic", "asyn message".getBytes()), new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("发送失败");
            }
        });

        //（单向消息）直接发出去，不关注结果
        PRODUCER.sendOneway(new Message("MyTopic", "one way message".getBytes()));
        PRODUCER.sendOneway(new Message("MyTopic", "tag", "key", "one way message".getBytes()));

        //PRODUCER.shutdown();
    }

    /**
     * 发送单个消息(同步)
     * @param topic
     * @param body
     * @return
     */
    public static SendResult sendSingle(String topic, String body) {
        Message msg = new Message(topic, body.getBytes());
        try {
            return PRODUCER.send(msg);
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
    }

    /**
     * 发送多条消息(异步)
     * @param topic
     * @param bodys
     * @return
     */
    public static SendResult sendMultiple(String topic, List<String> bodys) {
        try {
            return PRODUCER.send(bodys.stream().map(item -> new Message(topic, item.getBytes())).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
    }

}
