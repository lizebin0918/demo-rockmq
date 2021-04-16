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
    * broker
    * 启动:nohup sh bin/mqbroker -n localhost:9876 autoCreateTopicEnable=true -c conf/broker.conf &
    * 停止:sh bin/mqshutdown broker

  * 安装console
    * console安装目录:/root/home/rocketmq-externals/rocketmq-console/target
    * 启动:nohup java -jar rocketmq-console-ng-1.0.1.jar &

  * 问题记录
    * client无法send(msg)，但是console后台可以，由于为配置：`/root/home/rocketmq-4.8/distribution/target/rocketmq-4.8.0/rocketmq-4.8.0/conf`
    * 默认添加两个地址，用于注册到name service

        > brokerIP1=192.168.56.100
        > brokerIP2=192.168.56.100

## 组件介绍

  * name server:服务注册中心，无状态，信息存储在内存里，并不会协同数据
  * broker:面向producer和consumer，启动会注册到name server
  * producer:生产者
  * consumer:消费者
  * topic:消息类型，一个topic可以有多个queue，一个queue分布在多个Broker上

  > Queue是Topic在一个Broker上的分片等分为指定份数后的其中一份，是负载均衡过程中资源分配的基本单元

