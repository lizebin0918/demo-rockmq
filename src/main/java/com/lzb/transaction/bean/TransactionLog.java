package com.lzb.transaction.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 本地事件表<br/>
 * Created on : 2021-04-18 21:27
 * @author lizebin
 */
@Data
@TableName("t_transaction_log")
public class TransactionLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String businessType;

    private String businessId;

    private String transactionId;

    private Date ctime;

    private Date ltime;

}
