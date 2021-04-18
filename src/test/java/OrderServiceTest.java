import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzb.Application;
import com.lzb.transaction.bean.Order;
import com.lzb.transaction.mapper.OrderMapper;
import com.lzb.transaction.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * <br/>
 * Created on : 2020-05-22 16:05
 * @author chenpi 
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrderServiceTest {

    @Resource
    private OrderService orderService;

    @Resource
    private OrderMapper orderMapper;

    @Test
    public void test() throws InterruptedException {
        Order order = new Order();
        order.setPayMoney(100);
        order.setUserId(1);
        System.out.println(orderService.order(order));

        Thread.sleep(10000000);
    }

}
