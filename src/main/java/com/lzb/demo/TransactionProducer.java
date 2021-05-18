package com.lzb.demo;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * <br/>
 * Created on : 2021-04-18 18:15
 * @author lizebin
 */
public class TransactionProducer {

    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        TransactionMQProducer tp = new TransactionMQProducer("transaction-producer-1");
        tp.setNamesrvAddr("192.168.56.100:9876");
        tp.setSendMsgTimeout(60000);
        tp.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {

                System.out.println(" executeLocalTransaction message.body = " + JSON.toJSONString(new String(message.getBody())));
                System.out.println(" executeLocalTransaction transactionId = " + message.getTransactionId());

                //执行本地事务
                return LocalTransactionState.UNKNOW;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {

                System.out.println(" checkLocalTransaction message.body = " + JSON.toJSONString(new String(messageExt.getBody())));
                System.out.println(" checkLocalTransaction transactionId = " + messageExt.getTransactionId());

                //broker回调检查事务
                //return LocalTransactionState.UNKNOW;
                //return LocalTransactionState.COMMIT_MESSAGE;
                return LocalTransactionState.UNKNOW;
            }
        });
        tp.start();
        tp.sendMessageInTransaction(new Message("transaction", "asyn message".getBytes()), "arg");
    }



}
