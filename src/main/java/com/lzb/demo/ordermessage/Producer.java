package com.lzb.demo.ordermessage;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <br/>
 * Created on : 2021-04-15 08:18
 *
 * @author lizebin
 */
public class Producer {

    private static final DefaultMQProducer PRODUCER = new DefaultMQProducer("ordermessage-" + "producerGroup");

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
        String topic = "ordermessage";

        List<OrderStep> list = OrderStep.buildOrders();
        list.sort(Comparator.comparing(OrderStep::getGroupId).thenComparing(Comparator.comparing(OrderStep::getOrderId)));

        for (OrderStep orderStep : list) {
            Message message = new Message(topic, "Order",
                                          Objects.toString(orderStep.getGroupId()) + Objects.toString(orderStep.getOrderId()),
                                          JSON.toJSONString(orderStep).getBytes());
            /**
             * msg:消息对象:通过对消息的key，进行hash，相同hash的消息会被分配到同一个分区里面
             * 参数二：消息队列选择器
             * 参数三：选择业务标识
             */
            SendResult sendResult = PRODUCER.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    long groupId = (long)arg;
                    System.out.println(JSON.toJSONString(mqs));
                    //表示同一个groupId放到同一个队列
                    return mqs.get((int)groupId % mqs.size());
                }
            }, orderStep.getGroupId());

            System.out.println(JSON.toJSONString(sendResult));
        }

        PRODUCER.shutdown();

    }


}
