package com.lzb.transaction.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzb.transaction.bean.Order;
import com.lzb.transaction.bean.TransactionLog;
import com.lzb.transaction.mapper.TransactionLogMapper;
import com.lzb.transaction.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 事务执行<br/>
 * Created on : 2021-04-18 21:10
 * @author lizebin
 */
@Slf4j
@Component
public class OrderTransactionListener implements TransactionListener {

    @Resource
    private OrderService orderService;

    @Resource
    private TransactionLogMapper transactionLogMapper;

    /**
     * 发送 half msg 返回ok，执行本地事务
     * @param message
     * @param o
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        LocalTransactionState state = LocalTransactionState.UNKNOW;
        log.info("开启订单本地事务");
        try {
            Order order = JSONObject.parseObject(new String(message.getBody()), Order.class);
            orderService.order(message.getTransactionId(), order);
            //half msg 消息确认
            state = LocalTransactionState.COMMIT_MESSAGE;
            //half msg 待确认
            //state = LocalTransactionState.UNKNOW;
        } catch (Exception e) {
            log.info("执行订单本地事务有误", e);
            state = LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return state;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        log.info("开始检查本地事务表状态：{}", messageExt.getTransactionId());
        LocalTransactionState state = LocalTransactionState.UNKNOW;
        //如果本地事务表有此记录，则表示插入成功
        LambdaQueryWrapper<TransactionLog> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TransactionLog::getTransactionId,  messageExt.getTransactionId());
        if (transactionLogMapper.selectCount(wrapper) > 0) {
            state = LocalTransactionState.COMMIT_MESSAGE;
        }
        log.info("结束检查本地事务状态：{}--->{}", messageExt.getTransactionId(), state);
        return state;
    }
}
