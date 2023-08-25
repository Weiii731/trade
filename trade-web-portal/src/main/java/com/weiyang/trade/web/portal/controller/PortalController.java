package com.weiyang.trade.web.portal.controller;

import com.alibaba.fastjson.JSON;
import com.weiyang.trade.goods.db.model.Goods;
import com.weiyang.trade.goods.service.GoodsService;
import com.weiyang.trade.web.portal.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class PortalController {

    @Autowired
    GoodsService goodsService;

    /**
     * Product Detail Page
     * @param goodsId - product id
     * @return - model and view
     */
    @RequestMapping("/goods/{goodsId}")
    public ModelAndView productPage(@PathVariable long goodsId) {
        Goods product = goodsService.selectGoods(goodsId);
        log.info("goodsId={}, goods={}", goodsId, JSON.toJSON(product));
        String showPrice = CommonUtils.changeF2Y(product.getPrice());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("goods", product);
        modelAndView.addObject("showPrice", showPrice);
        modelAndView.setViewName("goods_detail");
        return modelAndView;
    }
}
