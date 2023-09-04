package com.weiyang.trade.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.weiyang.trade.goods.db.model.Goods;
import com.weiyang.trade.goods.service.GoodsService;
import com.weiyang.trade.goods.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient client;
    @Override
    public void addGoodsToES(Goods goods) {
        try {
            String data = JSON.toJSONString(goods);
            IndexRequest request = new IndexRequest("goods").source(data, XContentType.JSON);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            log.info("AddGoodsToES result: {}", response);
        } catch (Exception e) {
            log.error("SearchService addGoods error", e);
        }
    }

    @Override
    public List<Goods> searchGoodsList(String keyword, int from, int size) {
        try {
            SearchRequest searchRequest = new SearchRequest("goods");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, "title", "description", "keywords");
            searchSourceBuilder.query(queryBuilder);

            searchSourceBuilder.from(from);
            searchSourceBuilder.size(size);

            /*
             * setting sorting rules
             * sort based on sales num
             */
            searchSourceBuilder.sort("saleNum", SortOrder.DESC);

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//            System.out.println(JSON.toJSONString(searchResponse));

            SearchHits hits = searchResponse.getHits();
            List<Goods> goodsList = new ArrayList<>();
            for (SearchHit searchHit : hits) {
                String sourceAsString = searchHit.getSourceAsString();
                Goods goods = JSON.parseObject(sourceAsString, Goods.class);
                goodsList.add(goods);
            }
            log.info("search result {}", JSON.toJSONString(goodsList));
            return goodsList;
        } catch (Exception e) {
            log.error("SerachService searchGoodsList error", e);
            return null;
        }
    }

}
