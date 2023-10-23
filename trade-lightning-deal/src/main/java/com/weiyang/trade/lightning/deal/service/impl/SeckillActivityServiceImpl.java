package com.weiyang.trade.lightning.deal.service.impl;

import com.alibaba.fastjson.JSON;
import com.weiyang.trade.lightning.deal.db.dao.SeckillActivityDao;
import com.weiyang.trade.lightning.deal.db.model.SeckillActivity;
import com.weiyang.trade.lightning.deal.service.SeckillActivityService;
import com.weiyang.trade.order.db.model.Order;
import com.weiyang.trade.order.mq.OrderMessageSender;
import com.weiyang.trade.order.service.LimitBuyService;
import com.weiyang.trade.order.utils.RedisWorker;
import com.weiyang.trade.order.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SeckillActivityServiceImpl implements SeckillActivityService {

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private RedisWorker redisWorker;

    @Autowired
    private LimitBuyService limitBuyService;

    /**
     * datacenterId;  数据中心
     * machineId;     机器标识
     * 在分布式环境中可以从机器配置上读取
     * 单机开发环境中先写死
     */
    final private SnowflakeIdWorker snowFlake = new SnowflakeIdWorker(6, 8);

    @Autowired
    private OrderMessageSender orderMessageSender;

    @Override
    public boolean insertSeckillActivity(SeckillActivity seckillActivity) {
        return seckillActivityDao.insertSeckillActivity(seckillActivity);
    }


    @Override
    public SeckillActivity querySeckillActivityById(long id) {
        return seckillActivityDao.querySeckillActivityById(id);
    }

    @Override
    public List<SeckillActivity> queryActivitysByStatus(int status) {
        return seckillActivityDao.queryActivitysByStatus(status);
    }

    /**
     * 处理秒杀请求
     * 高并发时会出现超卖
     *
     * @param seckillActivityId
     * @return
     */
    @Override
    public boolean processSeckillReqBase(long seckillActivityId) {
        // check redis first
        String key = "ActivityId: " + seckillActivityId;
        boolean checkResult = redisWorker.stockDeductCheck(key);
        if (!checkResult) {
            return false;
        }

        //1.查询对应的秒杀活动信息
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);

        if (seckillActivity == null) {
            log.error("seckillActivityId={} 查询不到对应的秒杀活动", seckillActivityId);
            throw new RuntimeException("查询不到对应的秒杀活动");
        }

        int availableStock = seckillActivity.getAvailableStock();
        if (availableStock > 0) {
            log.info("商品抢购成功");
            seckillActivityDao.updateAvailableStockByPrimaryKey(seckillActivityId);
            return true;
        } else {
            log.info("商品抢购失败，商品已经售完");
            return false;
        }
    }

    @Override
    public Order processLightningDeal(long userId, long activityID) {
        // 1. 查看userid是否有限购
        boolean result = limitBuyService.isInLimitMember(activityID, userId);
        if (result) {
            log.error("当前用户已经购买过不能重复购买 ActivityId={}, userId={}", activityID, userId);
            throw new RuntimeException("当前用户已经购买过不能重复购买");
        }

        // 2. 查询对应秒杀活动消息
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(activityID);
        if (seckillActivity == null) {
            log.error("活动不存在");
            throw new RuntimeException("活动不存在");
        }

        // 3. Redis中检查库存是否存在
        String key = "ActivityId: " + activityID;
        boolean checkResult = redisWorker.stockDeductCheck(key);
        if (!checkResult) {
            log.error("库存不足 购买失败, ActivityId={}", activityID);
            throw new RuntimeException("库存不足 购买失败");
        }

        // 4. 数据库中锁定库存
        boolean lockStockResult = seckillActivityDao.lockStock(activityID);
        if (!lockStockResult) {
            log.error("锁定库存失败 商品已售罄, ActivityId={}", activityID);
            throw new RuntimeException("库存不足 购买失败");
        } else {
            log.info("商品抢购成功 AcitvityId={}, userId:{}", activityID, userId);
        }

        // 5. 创建订单
        Order order = new Order();
        order.setId(snowFlake.nextId());
        order.setActivityId(activityID);
        order.setUserId(userId);
        order.setGoodsId(seckillActivity.getGoodsId());
        order.setPayPrice(seckillActivity.getSeckillPrice());
        order.setActivityType(1);
        order.setStatus(1);
        order.setCreateTime(new Date());

        // 6. 发送创建订单信息到 MQ
        orderMessageSender.sendCreateOrderMessage(JSON.toJSONString(order));
        return order;
    }

    @Override
    public boolean lockStock(long activityId) {
        log.info("秒杀活动锁定库存 seckillActivityId:{}", activityId);
        return seckillActivityDao.lockStock(activityId);
    }

    @Override
    public boolean deductStock(long activityId) {
        log.info("秒杀活动扣减库存 seckillActivityId:{}", activityId);
        return seckillActivityDao.deductStock(activityId);
    }

    @Override
    public boolean revertStock(long activityId) {
        log.info("秒杀活动回补库存 seckillActivityId:{}", activityId);
        return seckillActivityDao.revertStock(activityId);
    }

}
