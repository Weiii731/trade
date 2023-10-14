package com.weiyang.trade.lightning.deal;


import com.weiyang.trade.lightning.deal.db.dao.SeckillActivityDao;
import com.weiyang.trade.lightning.deal.db.model.SeckillActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LightningDealTest {

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Test
    public void insertGoodsTest() {
        System.out.println("Hello");
        SeckillActivity seckillActivity = new SeckillActivity();
        seckillActivity.setActivityName("秒杀活动Test");
        seckillActivity.setGoodsId(123L);

        seckillActivity.setStartTime(new Date());
        seckillActivity.setEndTime(new Date());
        seckillActivity.setAvailableStock(100);
        //默认上架
        seckillActivity.setActivityStatus(1);
        //初始为0
        seckillActivity.setLockStock(0);
        seckillActivity.setSeckillPrice(999);
        seckillActivity.setOldPrice(888);
        seckillActivity.setCreateTime(new Date());
        seckillActivityDao.insertSeckillActivity(seckillActivity);
    }
}
