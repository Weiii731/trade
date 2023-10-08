package com.weiyang.trade.order.mq;

import com.alibaba.fastjson.JSON;
import com.weiyang.trade.goods.service.GoodsService;
import com.weiyang.trade.order.db.dao.OrderDao;
import com.weiyang.trade.order.db.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 消息消费类，订单支付状态交易
 */

@Component
@Slf4j
public class OrderPayCheckReceiver {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsService goodsService;

    /**
     * 消息处理
     * @param msg
     */
    @RabbitListener(queues = "order.pay.status.check.queue")
    public void process(String msg) {
        log.info("接收到消息内容:{}", msg);
        Order order = JSON.parseObject(msg, Order.class);
        // 1.查询订单消息
        Order orderInfo = orderDao.queryOrderById(order.getId());
        if (orderInfo.getStatus() == 1) {
            // 2. 判断是否完成支付
            log.info("订单{}超时支付, 关闭订单", orderInfo.getId());
            orderInfo.setStatus(99);
            // 3. 更新订单状态为关闭
            orderDao.updateOrder(orderInfo);
            // 4. 将锁定库存补回
            goodsService.revertStock(orderInfo.getGoodsId());
        }
    }
}
