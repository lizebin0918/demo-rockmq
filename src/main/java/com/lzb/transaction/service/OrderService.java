package com.lzb.transaction.service;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.lzb.transaction.bean.Order;
import com.lzb.transaction.bean.TransactionLog;
import com.lzb.transaction.config.TransactionProducer;
import com.lzb.transaction.constant.Constants;
import com.lzb.transaction.mapper.OrderMapper;
import com.lzb.transaction.mapper.TransactionLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 订单服务 <br/>
 * Created on : 2021-04-18 21:11
 * @author lizebin
 */
@Slf4j
@Service
public class OrderService {

    @Resource
    private TransactionProducer transactionProducer;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private TransactionLogMapper transactionLogMapper;

    private Snowflake snowflake = new Snowflake(1, 1);

    /**
     * 下单
     * @param transactionId
     * @param order
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String order(String transactionId, Order order) {
        //1.创建订单
        orderMapper.insert(order);

        //2.写入事务日志
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setBusinessId(order.getId());
        transactionLog.setBusinessType("order");
        transactionLog.setTransactionId(transactionId);
        transactionLogMapper.insert(transactionLog);

        log.info("订单创建完成。{}", JSON.toJSONString(order));
        return order.getId();
    }

    public String order(Order order) {
        order.setId(Objects.toString(snowflake.nextId()));
        order.setOrderNo(snowflake.nextIdStr());
        transactionProducer.send(JSON.toJSONString(order), Constants.TOPIC);
        return order.getOrderNo();
    }

}
