#《RocketMQ笔记》

## 安装
  * 下载
  
    * unzip rocketmq-all-4.8.0-source-release.zip
    * cd rocketmq-all-4.8.0/
    * mvn -Prelease-all -DskipTests clean install -U
    * cd distribution/target/rocketmq-4.8.0/rocketmq-4.8.0
    * mq安装目录:/root/home/rocketmq-4.8/distribution/target/rocketmq-4.8.0/rocketmq-4.8.0

  * 启动
    * 启动name server:nohup sh bin/mqnamesrv &
    * tail -f ~/logs/rocketmqlogs/namesrv.log
        > 出现关键字，表示启动成功--`The Name Server boot success. serializeType=JSON`
    * 启动 broker:nohup sh bin/mqbroker -n localhost:9876 autoCreateTopicEnable=true -c conf/broker.conf &
    * tail -f ~/logs/rocketmqlogs/broker.log
        > 出现关键字，表示启动成功--`The broker[broker-a, 192.168.56.100:10911] boot success. serializeType=JSON and name server is localhost:9876`
    * 停止:sh bin/mqshutdown broker

  * 安装console
    * console安装目录:/root/home/rocketmq-externals/rocketmq-console/target
    * 启动:nohup java -jar rocketmq-console-ng-1.0.1.jar &
    * 浏览器输入:`http://192.168.56.100:8080/#/`

  * 问题记录
    * client无法send(msg)，但是console后台可以，由于缺少配置：`/root/home/rocketmq-4.8/distribution/target/rocketmq-4.8.0/rocketmq-4.8.0/conf/broker.conf`
    * 默认添加两个地址，用于注册到name service
    
      ```
        brokerIP1=192.168.56.100
        brokerIP2=192.168.56.100
      ```
                                        
    * producer发送消息，不是直接找broker，而是找name server，找到对应的broker，最后再找到topic
    
## 组件介绍

  * name server:服务注册中心，无状态，信息存储在内存里，并不会协同数据，收集其他服务的信息
  * broker:面向producer和consumer，启动会注册到name server
  * producer:生产者
    * 消息类型（发送模式）：同步消息、异步消息、单向消息
    * 消费模式
      * 集群模式 -> 一组consumer
      * 广播模式 -> 多组comsumer
  * consumer:消费者（顺序消费，并行消费）
  * topic:消息类型，一个topic可以有多个queue，多个queue分布在多个Broker上
      > Queue是Topic在一个Broker上的分片等分为指定份数后的其中一份，是负载均衡过程中资源分配的基本单元
      > 多个Queue存在于不同的broker是分布式的，而不是集群复制

  * tag:用于消息过滤，消息分组，类似于筛选
  * key:
  
## 高可用机制
  * 两个master对应两个broker，相互隔离，不同步，各自分别有两个slave
  * master负责接收事务消息
  * slave负责读请求，所以必须配置slave
  
## 消息可靠
  * 消息衰减重试
  * DLQ死信队列
  * 根据重试次数，持久化到数据库
  
## 事务消息
  * 开启事务，producer 发送 half msg
  * broker 写入 half 队列，返回给producer
  * 执行本地事务，producer 发送rollback或者commit消息
  * 如果broker超时未收到确认，定时任务轮询half队列，回查producer状态
  * 如果确认commit，consumer就可以消费到
