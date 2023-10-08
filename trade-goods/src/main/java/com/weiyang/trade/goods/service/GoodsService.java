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

    /**
     * 查询商品信息
     *
     * @param id
     * @return
     */
    Goods queryGoodsById(long id);

    /**
     * 锁库存
     * @param id
     * @return
     */
    boolean lockStock(long id);

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
