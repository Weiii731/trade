package com.weiyang.trade.web.portal.controller;

import com.alibaba.fastjson.JSON;
import com.weiyang.trade.goods.db.model.Goods;
import com.weiyang.trade.goods.service.GoodsService;
import com.weiyang.trade.goods.service.SearchService;
import com.weiyang.trade.order.db.model.Order;
import com.weiyang.trade.order.service.OrderService;
import com.weiyang.trade.web.portal.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class PortalController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    SearchService searchService;

    @Autowired
    OrderService orderService;

    /**
     * 跳转到主页面
     *
     * @return
     */
    @RequestMapping("/goods_detail")
    public String index() {
        return "goods_detail";
    }

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

    /**
     * search page
     * @return
     */
    @RequestMapping("/search")
    public String searchPage() {
        return "search";
    }

    /**
     * 搜索查询
     *
     * @return
     */
    @RequestMapping("/searchAction")
    public String search(@RequestParam("searchWords") String searchWords, Map<String, Object> resultMap) {
        log.info("searchWords: {}", searchWords);
        List<Goods> goodsList = searchService.searchGoodsList(searchWords, 0, 10);
        resultMap.put("goodsList", goodsList);
        return "search";
    }

    /**
     * 购买
     * @param resultMap
     * @param userId
     * @param goodsId
     * @return
     */
    @RequestMapping("/buy/{userId}/{goodsId}")
    public String buy(Map<String, Object> resultMap, @PathVariable long userId, @PathVariable long goodsId) {
        log.info("userId={}, goodsId={}", userId, goodsId);
        Order order = orderService.createOrder(userId, goodsId);
        resultMap.put("order", order);
        resultMap.put("resultInfo", "下单成功");
        return "buy_result";
    }

    /**
     * 订单详情查询
     * @param resultMap
     * @param orderId
     * @return
     */
    @RequestMapping("/order/query/{orderId}")
    public String orderQuery(Map<String, Object> resultMap, @PathVariable long orderId) {
        try {
            Order order = orderService.queryOrder(orderId);
            log.info("orderId={} order={}", orderId, JSON.toJSON(order));
            String orderShowPrice = CommonUtils.changeF2Y(order.getPayPrice());
            resultMap.put("order", order);
            resultMap.put("orderShowPrice", orderShowPrice);
            return "order_detail";
        } catch (Exception e) {
            resultMap.put("errorInfo", e.getMessage());
            return "error";
        }

    }

    /**
     * 订单支付
     *
     * @return
     */
    @RequestMapping("/order/payOrder/{orderId}")
    public String payOrder(Map<String, Object> resultMap, @PathVariable long orderId) throws Exception {
        try {
            orderService.payOrder(orderId);
            return "redirect:/order/query/" + orderId;
        } catch (Exception e) {
            log.error("payOrder error,errorMessage:{}", e.getMessage());
            resultMap.put("errorInfo", e.getMessage());
            return "error";
        }
    }
}
