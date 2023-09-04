package com.weiyang.trade.order.mq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderMessageSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendPayStatusCheckDelayMessage(String msg) {
        log.info("发送订单确认消息:{}", msg);
        amqpTemplate.convertAndSend("order-event-exchange", "order.create", msg);
    }
}
