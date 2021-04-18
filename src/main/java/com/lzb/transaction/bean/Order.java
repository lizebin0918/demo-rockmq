package com.lzb.transaction.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;


/**
 * 订单实体<br/>
 * Created on : 2021-04-18 21:14
 * @author lizebin
 */
@Data
@TableName("t_order")
public class Order {

    private String id;

    private String orderNo;

    private Integer payMoney;

    private Integer userId;

    private Date ctime;

    private Date ltime;
}
