package com.lzb.demo.ordermessage;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 订单步骤<br/>
 * Created on : 2021-05-18 22:49
 * @author lizebin
 */
@Data
public class OrderStep {

    private long orderId;
    private long groupId;
    private String desc;

    public static List<OrderStep> buildOrders() {
        List<OrderStep> list = new LinkedList<>();

        /*下单、推送、完成*/

        OrderStep order1 = new OrderStep();
        OrderStep order2 = new OrderStep();
        OrderStep order3 = new OrderStep();

        order1.setDesc("下单");
        order1.setGroupId(1);
        order1.setOrderId(1);
        list.add(order1);

        order1 = new OrderStep();
        order1.setDesc("推送");
        order1.setGroupId(1);
        order1.setOrderId(2);
        list.add(order1);

        order2.setDesc("下单");
        order2.setGroupId(2);
        order2.setOrderId(1);
        list.add(order2);

        order1 = new OrderStep();
        order1.setDesc("完成");
        order1.setGroupId(1);
        order1.setOrderId(3);
        list.add(order1);

        order3.setDesc("下单");
        order3.setGroupId(3);
        order3.setOrderId(1);
        list.add(order3);

        order1.setDesc("推送");
        order1.setGroupId(2);
        order1.setOrderId(2);
        list.add(order2);

        order3.setDesc("推送");
        order3.setGroupId(3);
        order3.setOrderId(2);
        list.add(order3);

        return list;
    }

}
