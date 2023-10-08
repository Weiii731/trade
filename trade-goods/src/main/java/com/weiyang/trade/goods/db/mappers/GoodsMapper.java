package com.weiyang.trade.goods.db.mappers;

import com.weiyang.trade.goods.db.model.Goods;

public interface GoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    int lockStock(Long id);

    int deductStock(Long id);

    int revertStock(Long id);
}