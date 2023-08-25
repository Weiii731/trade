package com.weiyang.trade.goods.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.weiyang.trade.goods.db.dao.GoodsDao;
import com.weiyang.trade.goods.db.model.Goods;
import com.weiyang.trade.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;


    @Override
    public Boolean insertGoods(Goods good) {
        return goodsDao.insertGoods(good);
    }

    @Override
    public Goods selectGoods(long id) {
        return goodsDao.queryGoodsById(id);
    }
}
