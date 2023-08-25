package com.weiyang.trade.goods;

import com.alibaba.fastjson.JSON;
import com.weiyang.trade.goods.db.dao.GoodsDao;
import com.weiyang.trade.goods.db.model.Goods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TradeGoodsApplicationTests {

	@Autowired
	private GoodsDao goodsDao;

	@Test
	void testInsertGoods() {
		Goods goods = new Goods();
		goods.setTitle("iphone 14 pro max");
		goods.setBrand("苹果 Apple");
		goods.setCategory("手机");
		goods.setNumber("NO123456");
		goods.setImage("test");
		goods.setDescription("iphone 14 pro max is very good");
		goods.setKeywords("苹果 手机 apple");
		goods.setSaleNum(0);
		goods.setAvailableStock(10000);
		goods.setPrice(999999);
		goods.setStatus(1);
		boolean result = goodsDao.insertGoods(goods);
		System.out.println(result);
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
		Goods goods = goodsDao.queryGoodsById(2L);
		goods.setTitle(goods.getTitle() + " update");
		goodsDao.updateGoods(goods);
	}

}
