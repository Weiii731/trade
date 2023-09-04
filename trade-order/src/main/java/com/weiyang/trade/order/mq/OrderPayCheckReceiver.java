package com.weiyang.trade.order.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 消息消费类，订单支付状态交易
 */

@Component
@Slf4j
public class OrderPayCheckReceiver {

    /**
     * 消息处理
     * @param msg
     */
    @RabbitListener(queues = "order.pay.status.check.queue")
    public void process(String msg) {
        log.info("接受时间: " + LocalDateTime.now() + " 接受内容: " + msg);
    }
}
