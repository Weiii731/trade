package com.weiyang.trade.goods.db.dao;

import com.weiyang.trade.goods.db.model.Goods;

public interface GoodsDao {

    /**
     * insert good
     * @param goods
     * @return
     */
    boolean insertGoods(Goods goods);

    /**
     * delete goods by id
     * @param id
     * @return
     */
    boolean deleteGoods(Long id);

    /**
     * update goods selective
     * @param goods
     * @return
     */
    boolean updateGoods(Goods goods);

    /**
     * query goods by id
     * @param id
     * @return
     */
    Goods queryGoodsById(Long id);
}
