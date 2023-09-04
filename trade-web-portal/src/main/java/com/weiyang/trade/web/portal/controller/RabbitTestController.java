package com.weiyang.trade.web.portal.controller;


import com.weiyang.trade.order.mq.OrderMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
@Slf4j
public class RabbitTestController {

    @Autowired
    OrderMessageSender orderMessageSender;

    @ResponseBody
    @RequestMapping("/delayTest")
    public String test() {
        orderMessageSender.sendPayStatusCheckDelayMessage("发送的时间: " + LocalDateTime.now() + "内容：延迟队列测试");
        return "send ok";
    }
}
