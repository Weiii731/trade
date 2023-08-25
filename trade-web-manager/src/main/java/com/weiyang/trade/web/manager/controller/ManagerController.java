package com.weiyang.trade.web.manager.controller;

import com.weiyang.trade.goods.db.dao.GoodsDao;
import com.weiyang.trade.goods.db.model.Goods;
import com.weiyang.trade.goods.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
public class ManagerController {

    @Autowired
    GoodsService goodsService;

    /**
     * homepage
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * add goods page
     * @return
     */
    @RequestMapping("/add_goods")
    public String addGoods() {
        return "add_goods";
    }

    /**
     * 处理商品添加
     * @param title
     * @param number
     * @param brand
     * @param image
     * @param description
     * @param price
     * @param keywords
     * @param category
     * @param stock
     * @param /resultMap
     * @return
     */
    @RequestMapping("/addGoodsAction")
    public String addGoodsAction(@RequestParam("title") String title,
                                 @RequestParam("number") String number,
                                 @RequestParam("brand") String brand,
                                 @RequestParam("image") String image,
                                 @RequestParam("description") String description,
                                 @RequestParam("price") Integer price,
                                 @RequestParam("keywords") String keywords,
                                 @RequestParam("category") String category,
                                 @RequestParam("stock") Integer stock,
                                 Map<String, Object> resultMap) {
        Goods goods = new Goods();
        goods.setTitle(title);
        goods.setNumber(number);
        goods.setBrand(brand);
        goods.setImage(image);
        goods.setDescription(description);
        goods.setPrice(price);
        goods.setKeywords(keywords);
        goods.setCategory(category);
        goods.setAvailableStock(stock);
        //初始为上架状态
        goods.setStatus(1);
        //初始的销售数量为0
        goods.setSaleNum(0);
        goods.setCreateTime(new Date());
        Boolean result = goodsService.insertGoods(goods);
        log.info("add goods /result={}", result);
        resultMap.put("goodsInfo", goods);
        return "add_goods";
    }
}
