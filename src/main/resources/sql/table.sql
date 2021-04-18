create table t_order
(
    id        varchar(32) not null primary key,
    order_no  char(32)    not null comment '订单号',
    pay_money int(11)     not null comment '支付金额',
    user_id   int(11)     not null comment '用户id',
    `ctime`   timestamp   NOT NULL DEFAULT current_timestamp(),
    `ltime`   timestamp   NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) comment '订单表';

create table transaction_log
(
    id             int(11)     not null primary key auto_increment,
    business_type  varchar(32) not null comment '事件类型',
    business_id    varchar(32) not null comment '业务主键-id',
    transaction_id char(32)    not null comment '事务id',
    `ctime`        timestamp   NOT NULL DEFAULT current_timestamp(),
    `ltime`        timestamp   NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) comment '事务事件表';

create table t_integration
(
    id       int(11)   not null primary key auto_increment,
    order_no char(32)  not null comment '订单号',
    user_id  int(11)   not null comment '用户id',
    `ctime`  timestamp NOT NULL DEFAULT current_timestamp(),
    `ltime`  timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) comment '积分流水表';