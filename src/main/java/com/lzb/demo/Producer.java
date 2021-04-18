package com.lzb.demo;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
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
        PRODUCER.shutdown();
        System.out.println(JSON.toJSONString(sendResult));
    }

    /**
     * 发送单个消息
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
     * 发送多条消息
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
