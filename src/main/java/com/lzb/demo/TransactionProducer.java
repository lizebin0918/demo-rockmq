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

    /**
     * 发送方将半事务消息发送至消息队列RocketMQ版服务端。
     * 消息队列RocketMQ版服务端将消息持久化成功之后，向发送方返回Ack确认消息已经发送成功，此时消息为半事务消息。
     * 发送方开始执行本地事务逻辑。
     * 发送方根据本地事务执行结果向服务端提交二次确认（Commit或是Rollback），服务端收到Commit状态则将半事务消息标记为可投递，订阅方最终将收到该消息；服务端收到Rollback状态则删除半事务消息，订阅方将不会接受该消息。
     *
     * 在断网或者是应用重启的特殊情况下，上述步骤4提交的二次确认最终未到达服务端，经过固定时间后服务端将对该消息发起消息回查。
     * 发送方收到消息回查后，需要检查对应消息的本地事务执行的最终结果。
     * 发送方根据检查得到的本地事务的最终状态再次提交二次确认，服务端仍按照步骤4对半事务消息进行操作。
     * @param args
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     */
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
