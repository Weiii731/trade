package com.weiyang.trade.lightning.deal.db.dao.impl;

import com.weiyang.trade.lightning.deal.db.dao.SeckillActivityDao;
import com.weiyang.trade.lightning.deal.db.mappers.SeckillActivityMapper;
import com.weiyang.trade.lightning.deal.db.model.SeckillActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckillActivityDaoImpl implements SeckillActivityDao {

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Override
    public boolean insertSeckillActivity(SeckillActivity seckillActivity) {
        int result = seckillActivityMapper.insert(seckillActivity);
        //大于0 表示插入成功
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public SeckillActivity querySeckillActivityById(long id) {
        return seckillActivityMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SeckillActivity> queryActivitysByStatus(int status) {
        return seckillActivityMapper.queryActivitysByStatus(status);
    }

    @Override
    public boolean updateAvailableStockByPrimaryKey(long id) {
        int result = seckillActivityMapper.updateAvailableStockByPrimaryKey(id);
        return result > 0;
    }

    @Override
    public boolean lockStock(Long id) {
        int result = seckillActivityMapper.lockStock(id);
        return result > 0;
    }

    @Override
    public boolean deductStock(Long id) {
        int result = seckillActivityMapper.deductStock(id);
        return result > 0;
    }

    @Override
    public boolean revertStock(Long id) {
        int result = seckillActivityMapper.revertStock(id);
        return result > 0;
    }


}
