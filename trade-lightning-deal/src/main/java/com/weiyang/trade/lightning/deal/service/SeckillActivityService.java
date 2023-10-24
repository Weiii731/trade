package com.weiyang.trade.lightning.deal.service;

import com.weiyang.trade.lightning.deal.db.model.SeckillActivity;
import com.weiyang.trade.order.db.model.Order;

import java.util.List;

public interface SeckillActivityService {

    /**
     * 插入一个秒杀活动
     *
     * @param seckillActivity
     * @return
     */
    boolean insertSeckillActivity(SeckillActivity seckillActivity);

    /**
     * 查询秒杀活动
     *
     * @param id
     * @return
     */
    SeckillActivity querySeckillActivityById(long id);

    List<SeckillActivity> queryActivitysByStatus(int status);

    /**
     * 处理秒杀请求
     *
     * @param seckillActivityId
     * @return
     */
    boolean processSeckillReqBase(long seckillActivityId);

    /**
     * 处理秒杀请求
     * 通过 Redis Lua 脚步进行效验
     * @param userId
     * @param activityID
     * @return Order
     */
    Order processLightningDeal(long userId, long activityID);

    /**
     * 锁定商品的库存
     *
     * @param activityId
     * @return
     */
    boolean lockStock(long activityId);

    /**
     * 库存扣减
     *
     * @param activityId
     * @return
     */
    boolean deductStock(long activityId);

    /**
     * 锁定的库存回补
     * @param activityId
     * @return
     */
    boolean revertStock(long activityId);

    /**
     * 缓存预热
     *  将秒杀信息写入Redis中
     * @param id
     */
    void  pushSeckillActivityInfoToCache(long id);
}
