package com.weiyang.trade.goods.service;


import com.weiyang.trade.goods.db.model.Goods;

import java.util.List;

public interface SearchService {

    /**
     * Add product to ES
     * @param goods
     */
    void addGoodsToES(Goods goods);


    /**
     * search based on keywords
     * @param keyword
     * @param from
     * @param size
     * @return
     */
    List<Goods> searchGoodsList(String keyword, int from, int size);
}
