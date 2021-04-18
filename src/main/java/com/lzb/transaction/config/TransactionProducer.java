package com.lzb.transaction.config;

import com.lzb.transaction.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * 生产者配置<br/>
 * Created on : 2021-04-18 21:01
 * @author lizebin
 */
@Slf4j
@Component
public class TransactionProducer {

    private static final String PRODUCER_GROUP = "order_trans_group";

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));

    private TransactionMQProducer producer;

    @Autowired
    private OrderTransactionListener orderTransactionListener;

    @PostConstruct
    public void init() {
        producer = new TransactionMQProducer(PRODUCER_GROUP);
        producer.setNamesrvAddr(Constants.MQ_URL);
        producer.setSendMsgTimeout(Integer.MAX_VALUE);
        producer.setExecutorService(executor);
        producer.setTransactionListener(orderTransactionListener);
        this.start();
    }

    private void start() {
        try {
            this.producer.start();
        } catch (MQClientException e) {
            log.error("订单生产者启动异常", e);
        }
    }

    public TransactionSendResult send(String data, String topic) {
        Message msg = new Message(topic, data.getBytes());
        try {
            return this.producer.sendMessageInTransaction(msg, null);
        } catch (MQClientException e) {
            log.error("订单生产者发送消息异常", e);
        }
        return null;
    }

}
