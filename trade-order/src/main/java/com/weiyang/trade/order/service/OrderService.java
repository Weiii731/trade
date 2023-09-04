package com.weiyang.trade.order.service;

import com.weiyang.trade.order.db.model.Order;

public interface OrderService {
    /**
     * 创建订单
     * @param userId
     * @param goodsId
     * @return
     */
    Order createOrder(long userId, long goodsId);

    /**
     * 订单查询
     * @param orderId
     * @return
     */
    Order queryOrder(long orderId);

    /**
     * 支付订单
     * @param orderId
     */
    void payOrder(long orderId);
}
