package com.weiyang.trade.order.db.dao;

import com.weiyang.trade.order.db.model.Order;

public interface OrderDao {
    /**
     * 插入一个订单
     *
     * @param order
     * @return
     */
    boolean insertOrder(Order order);

    /**
     * 根据ID删除对应的订单
     *
     * @param id
     * @return
     */
    boolean deleteOrder(long id);

    /**
     * 根据ID查询对应的订单
     *
     * @param id
     * @return
     */
    Order queryOrderById(long id);

    /**
     * 修改对应的订单信息
     *
     * @param order
     * @return
     */
    boolean updateOrder(Order order);
}
