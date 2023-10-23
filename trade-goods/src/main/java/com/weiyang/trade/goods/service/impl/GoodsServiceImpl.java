package com.weiyang.trade.goods.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.weiyang.trade.goods.db.dao.GoodsDao;
import com.weiyang.trade.goods.db.model.Goods;
import com.weiyang.trade.goods.service.GoodsService;
import com.weiyang.trade.goods.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private SearchService searchService;


    @Override
    public Boolean insertGoods(Goods good) {

        // add to db
        boolean res = goodsDao.insertGoods(good);
        // add to ES
        searchService.addGoodsToES(good);

        return res;
    }

    @Override
    public Goods selectGoods(long id) {
        return goodsDao.queryGoodsById(id);
    }

    @Override
    public Goods queryGoodsById(long id) {
        return goodsDao.queryGoodsById(id);
    }

    @Override
    public boolean lockStock(long id) {
        return goodsDao.lockStock(id);
    }

    @Override
    public boolean deductStock(Long id) {
        return goodsDao.deductStock(id);
    }

    @Override
    public boolean revertStock(Long id) {
        return goodsDao.revertStock(id);
    }
}
