package com.weiyang.trade.lightning.deal.db.dao;
import com.weiyang.trade.lightning.deal.db.model.SeckillActivity;

import java.util.List;

public interface SeckillActivityDao {


    /**
     * 插入一个秒杀活动
     *
     * @param seckillActivity
     * @return
     */
    boolean insertSeckillActivity(SeckillActivity seckillActivity);


    /**
     * 查询秒杀活动
     * @param id
     * @return
     */
    SeckillActivity querySeckillActivityById(long id);

    /**
     * 根据活动状态查询秒杀活动列表
     * @param status
     * @return
     */
    List<SeckillActivity> queryActivitysByStatus(int status);
}
