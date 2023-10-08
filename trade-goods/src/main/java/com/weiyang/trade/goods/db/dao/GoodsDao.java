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

    /**
     * lock stock
     * @param id
     * @return
     */
    boolean lockStock(Long id);

    /**
     * 删减库存
     * @param id
     * @return
     */
    boolean deductStock(Long id);

    /**
     * 订单超时 回滚库存
     * @param id
     * @return
     */
    boolean revertStock(Long id);
}
