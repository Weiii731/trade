package com.weiyang.trade.lightning.deal.mq;

import com.alibaba.fastjson.JSON;
import com.weiyang.trade.lightning.deal.service.SeckillActivityService;
import com.weiyang.trade.order.db.dao.OrderDao;
import com.weiyang.trade.order.db.model.Order;
import com.weiyang.trade.order.service.LimitBuyService;
import com.weiyang.trade.order.utils.RedisWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SeckillPayTimeOutReceiver {

    @Autowired
    SeckillActivityService seckillActivityService;

    @Autowired
    OrderDao orderDao;

    @Autowired
    LimitBuyService limitBuyService;

    @Autowired
    RedisWorker redisWorker;

    @RabbitListener(queues = "seckill.order.pay.status.check.queue")
    public void process(String msg) {
        log.info("接收到消息内容:{}", msg);
        Order order = JSON.parseObject(msg, Order.class);
        if (order.getActivityType() != 1) {
            return;
        }
        // 1.查询订单消息
        Order orderInfo = orderDao.queryOrderById(order.getId());
        if (orderInfo.getStatus() == 1) {
            // 2. 判断是否完成支付
            log.info("订单{}超时支付, 关闭订单", orderInfo.getId());
            orderInfo.setStatus(99);
            // 3. 更新订单状态为关闭
            orderDao.updateOrder(orderInfo);
            // 4. 将锁定库存补回
            seckillActivityService.revertStock(orderInfo.getGoodsId());
            // 5. 移除限购名单
            limitBuyService.removeLimitMember(order.getActivityId(), order.getUserId());
            // 6. 回滚redis里的库存
            // Todo - 回滚redis里的库存
        }
    }
}
