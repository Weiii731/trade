package com.weiyang.trade.web.portal.controller;

import com.alibaba.fastjson.JSON;
import com.weiyang.trade.goods.db.model.Goods;
import com.weiyang.trade.goods.service.GoodsService;
import com.weiyang.trade.goods.service.SearchService;
import com.weiyang.trade.lightning.deal.db.model.SeckillActivity;
import com.weiyang.trade.lightning.deal.service.SeckillActivityService;
import com.weiyang.trade.order.db.model.Order;
import com.weiyang.trade.order.service.OrderService;
import com.weiyang.trade.order.utils.RedisWorker;
import com.weiyang.trade.web.portal.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @Autowired
    SeckillActivityService seckillActivityService;

    @Autowired
    RedisWorker redisWorker;

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
    public ModelAndView buy(Map<String, Object> resultMap, @PathVariable long userId, @PathVariable long goodsId) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            log.info("userId={}, goodsId={}", userId, goodsId);
            Order order = orderService.createOrder(userId, goodsId);
            resultMap.put("order", order);
            resultMap.put("resultInfo", "下单成功");
            modelAndView.setViewName("buy_result");
            return modelAndView;
        } catch (Exception e) {
            // 下单失败
            log.error("buy error, errorMessage:{}", e.getMessage());
            modelAndView.addObject("resultInfo", "下单失败,原因" + e.getMessage());
            modelAndView.setViewName("buy_result");
            return modelAndView;
        }
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

    /**
     * 秒杀活动详情页
     *
     * @param resultMap
     * @param seckillId
     * @return
     */
    @RequestMapping("/seckill/{seckillId}")
    public String seckillInfo(Map<String, Object> resultMap, @PathVariable long seckillId) {
        try {

            SeckillActivity seckillActivity;
            String seckillActivityInfo = redisWorker.getValueByKey("seckillActivity:" + seckillId);
            if (!seckillActivityInfo.isEmpty()) {
                // 从缓存里调数据
                seckillActivity = JSON.parseObject(seckillActivityInfo, SeckillActivity.class);
                log.info("命中秒杀活动缓存:{}", seckillActivityInfo);
            } else {
                seckillActivity = seckillActivityService.querySeckillActivityById(seckillId);
            }
            if (seckillActivity == null) {
                log.error("秒杀的对应的活动信息 没有查询到 seckillId:{} ", seckillId);
                throw new RuntimeException("秒杀的对应的活动信息 没有查询到");
            }
            log.info("seckillId={},seckillActivity={}", seckillId, JSON.toJSON(seckillActivity));
            String seckillPrice = CommonUtils.changeF2Y(seckillActivity.getSeckillPrice());
            String oldPrice = CommonUtils.changeF2Y(seckillActivity.getOldPrice());

            Goods goods;
            String goodsInfo = redisWorker.getValueByKey("seckillActivity_goods:" + seckillActivity.getGoodsId());
            if (!goodsInfo.isEmpty()) {
                // 从缓存里调出数据
                goods = JSON.parseObject(goodsInfo, Goods.class);
                log.info("命中商品缓存:{}", goodsInfo);
            } else {
                goods = goodsService.queryGoodsById(seckillActivity.getGoodsId());
            }
            if (goods == null) {
                log.error("秒杀的对应的商品信息 没有查询到 seckillId:{} goodsId:{}", seckillId, seckillActivity.getGoodsId());
                throw new RuntimeException("秒杀的对应的商品信息 没有查询到");
            }

            resultMap.put("seckillActivity", seckillActivity);
            resultMap.put("seckillPrice", seckillPrice);
            resultMap.put("oldPrice", oldPrice);
            resultMap.put("goods", goods);
            return "seckill_item";
        } catch (Exception e) {
            log.error("获取秒杀信息详情页失败 get seckillInfo error,errorMessage:{}", e.getMessage());
            resultMap.put("errorInfo", e.getMessage());
            return "error";
        }
    }

    /**
     * 获取秒杀活动列表
     *
     * @param resultMap
     * @return
     */
    @RequestMapping("/seckill/list")
    public String activityList(Map<String, Object> resultMap) {
        List<SeckillActivity> seckillActivities = seckillActivityService.queryActivitysByStatus(1);
        resultMap.put("seckillActivities", seckillActivities);
        return "seckill_activity_list";
    }

    @ResponseBody
    @RequestMapping("/seckill/buy/{seckillId}")
    public String seckillInfoBase(@PathVariable long seckillId) {
        boolean res = seckillActivityService.processSeckillReqBase(seckillId);
        if (res) {
            return "商品抢购成功";
        } else {
            return "商品抢购失败, 商品已经售完";
        }
    }

    @RequestMapping("/seckill/buy/{userId}/{activityId}")
    public ModelAndView lightningDeal(@PathVariable long userId, @PathVariable long activityId) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            Order order = seckillActivityService.processLightningDeal(userId, activityId);
            modelAndView.addObject("resultInfo", "秒杀抢购成功");
            modelAndView.addObject("orderId", order.getId());
            modelAndView.setViewName("buy_result");
        } catch (Exception e) {
            modelAndView.addObject("errorInfo", e.getMessage());
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }
}
