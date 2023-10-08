package com.weiyang.trade.goods;

import com.alibaba.fastjson.JSON;
import com.weiyang.trade.goods.db.dao.GoodsDao;
import com.weiyang.trade.goods.db.model.Goods;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class GoodsTest {

    @Autowired
    private GoodsDao goodsDao;

    @Test
    void testInsertGoods() {
//		Goods goods = new Goods();
//		goods.setTitle("iphone 14 pro max");
//		goods.setBrand("苹果 Apple");
//		goods.setCategory("手机");
//		goods.setNumber("NO123456");
//		goods.setImage("test");
//		goods.setDescription("iphone 14 pro max is very good");
//		goods.setKeywords("苹果 手机 apple");
//		goods.setSaleNum(0);
//		goods.setAvailableStock(10000);
//		goods.setPrice(999999);
//		goods.setStatus(1);
//		boolean result = goodsDao.insertGoods(goods);
//		System.out.println(result);

        Goods goods = new Goods();
        goods.setTitle("华为mate50 pro");
        goods.setBrand("华为");
        goods.setCategory("手机");
        goods.setNumber("NO12360");
        goods.setImage("test");
        goods.setDescription("华为mate50 新品手机 曜金黑 8G+256G 全网通");
        goods.setKeywords("华为mate50 新品手机 曜金黑");
        goods.setSaleNum(58);
        goods.setAvailableStock(10000);
        goods.setPrice(899999);
        goods.setStatus(1);
        goodsDao.insertGoods(goods);
    }

    @Test
    public void deleteGoodsTest() {
        boolean deleteresult = goodsDao.deleteGoods(1L);
        System.out.println(deleteresult);
    }

    @Test
    public void queryGoodsTest() {
        Goods goods = goodsDao.queryGoodsById((long)16);
        System.out.println(JSON.toJSONString(goods));
    }

    @Test
    public void updateGoods() {
        Goods goods = goodsDao.queryGoodsById(16L);
        try {
            goods.setTitle(goods.getTitle() + " update");
            goodsDao.updateGoods(goods);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void lockStcok() {
        try {
            goodsDao.lockStock(21L);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
