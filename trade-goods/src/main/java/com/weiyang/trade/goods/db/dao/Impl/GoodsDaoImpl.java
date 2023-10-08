package com.weiyang.trade.goods.db.dao.Impl;

import com.weiyang.trade.goods.db.dao.GoodsDao;
import com.weiyang.trade.goods.db.mappers.GoodsMapper;
import com.weiyang.trade.goods.db.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GoodsDaoImpl implements GoodsDao {

    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public boolean insertGoods(Goods goods) {
        int result = goodsMapper.insert(goods);

        return result > 0;
    }

    @Override
    public boolean deleteGoods(Long id) {
        int result = goodsMapper.deleteByPrimaryKey(id);
        return result > 0;
    }

    @Override
    public boolean updateGoods(Goods goods) {
        int result = goodsMapper.updateByPrimaryKeySelective(goods);
        return result > 0;
    }

    @Override
    public Goods queryGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean lockStock(Long id) {
        int result = goodsMapper.lockStock(id);
        return result > 0;
    }

    @Override
    public boolean deductStock(Long id) {
        int result = goodsMapper.deductStock(id);
        return result > 0;
    }

    @Override
    public boolean revertStock(Long id) {
        int result = goodsMapper.revertStock(id);
        return result > 0;
    }
}
