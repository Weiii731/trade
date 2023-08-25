package com.weiyang.trade.goods.service;

import com.weiyang.trade.goods.db.model.Goods;

public interface GoodsService {
    /**
     * insert a good
     * @param good - goods
     * @return - int result
     */
    public Boolean insertGoods(Goods good);

    /**
     * select goods by id
     * @param id - goods' id
     * @return goods
     */
    public Goods selectGoods(long id);

}
