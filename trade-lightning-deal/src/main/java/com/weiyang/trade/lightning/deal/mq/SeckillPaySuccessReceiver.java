package com.weiyang.trade.lightning.deal.mq;

import com.alibaba.fastjson.JSON;
import com.weiyang.trade.lightning.deal.service.SeckillActivityService;
import com.weiyang.trade.order.db.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SeckillPaySuccessReceiver {

    @Autowired
    private SeckillActivityService seckillActivityService;

    @RabbitListener(queues = "seckill.order.pay.success.queue")
    public void process(String message) {
        log.info("秒杀支付成功消息处理，接收到消息内容:{}", message);
        Order order = JSON.parseObject(message, Order.class);
        seckillActivityService.deductStock(order.getActivityId());
    }
}