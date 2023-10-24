package com.weiyang.trade.web.portal.service;

import com.weiyang.trade.goods.db.model.Goods;
import com.weiyang.trade.goods.service.GoodsService;
import com.weiyang.trade.lightning.deal.db.model.SeckillActivity;
import com.weiyang.trade.lightning.deal.service.SeckillActivityService;
import com.weiyang.trade.web.portal.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 页面静态化创建服务
 */
@Service
@Slf4j
public class StaticPageService {

    @Autowired
    private SeckillActivityService seckillActivityService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TemplateEngine templateEngine;

    public void createStaticPage(long seckillActivityId) {
        PrintWriter writer = null;
        try {
            //1. 查询秒杀详情页所需数据
            SeckillActivity seckillActivity = seckillActivityService.querySeckillActivityById(seckillActivityId);
            Goods goodsInfo = goodsService.queryGoodsById(seckillActivity.getGoodsId());
            String seckillPrice = CommonUtils.changeF2Y(seckillActivity.getSeckillPrice());
            String oldPrice = CommonUtils.changeF2Y(seckillActivity.getOldPrice());

            // 2. 存放页面数据
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("seckillActivity", seckillActivity);
            resultMap.put("seckillPrice", seckillPrice);
            resultMap.put("oldPrice", oldPrice);
            resultMap.put("goods", goodsInfo);

            // 3. 创建thymeleaf 上下文对象
            Context context = new Context();
            // 4. 把数据放入上下文对象
            context.setVariables(resultMap);

            // 5. 创建所需要对应的HTML页面
            File file = new File("src/main/resources/templates/" + "seckill_item_" + seckillActivityId + ".html");
            writer = new PrintWriter(file);
            // 6. 执行模版引擎
            templateEngine.process("seckill_item", context, writer);
        } catch (Exception e) {
            log.error("创建静态化页面 error seckillActivityId={} ", seckillActivityId, e);
        } finally {
            //7.关闭输出流
            if (writer != null) {
                writer.close();
            }
        }
    }
}
