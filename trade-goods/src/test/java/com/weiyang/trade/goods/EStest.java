package com.weiyang.trade.goods;

import com.alibaba.fastjson.JSON;
import com.weiyang.trade.goods.model.Person;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EStest {

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testES() {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("http://127.0.0.1/", 9200, "http")
        ));
        System.out.println(JSON.toJSONString(client));
    }

    /**
     * 添加文档
     * @throws Exception
     */
    @Test
    public void addDoc() throws Exception {
        Person person = new Person();
//        person.setId("125");
        person.setName("林俊杰");
        person.setAddress("台湾台北");
        person.setAge(39);
        // convert to json
        String data = JSON.toJSONString(person);
        IndexRequest request = new IndexRequest("person").id(person.getId()).source(data, XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getId());
        System.out.println(JSON.toJSON(response));
    }

    /**
     * match all
     */
    @Test
    public void matchAll() throws IOException {

        //构建查询请求，指定查询的索引库
        SearchRequest searchRequest = new SearchRequest("person");
        //创建查询条件构造器 SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        /*
         * 构建查询条件
         * 查询所有文档
         */
        MatchAllQueryBuilder query = QueryBuilders.matchAllQuery();
        //指定查询条件
        searchSourceBuilder.query(query);
        /*
         * 指定分页查询信息
         * 从哪里开始查
         */
//        searchSourceBuilder.from(0);
//        //每次查询的数量
//        searchSourceBuilder.size(2);

        searchRequest.source(searchSourceBuilder);

        //查询获取查询结果
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response));

        // get hits
        SearchHits hits = response.getHits();
        long totalHits = hits.getTotalHits().value;
        System.out.println("totalHits: " + totalHits);

        List<Person> personList = new ArrayList<>();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            Person person = JSON.parseObject(sourceAsString, Person.class);
            personList.add(person);
        }

        System.out.println(JSON.toJSONString(personList));
    }

    /**
     * term词条查询
     */
    @Test
    public void matchTerm() throws IOException{
        SearchRequest searchRequest = new SearchRequest("person");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MatchQueryBuilder query = QueryBuilders.matchQuery("address", "台北");

        searchSourceBuilder.query(query);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        long totalHits = hits.getTotalHits().value;
        System.out.println("Total Hits: " + totalHits);

        List<Person> personList = new ArrayList<>();

        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            Person person = JSON.parseObject(sourceAsString, Person.class);
            personList.add(person);
        }

        System.out.println(JSON.toJSONString(personList));
    }

    @Test
    public void queryString() throws IOException {
        //构建查询请求，指定查询的索引库
        SearchRequest searchRequest = new SearchRequest("person");

        //创建查询条件构造器 SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryStringQueryBuilder query = QueryBuilders.queryStringQuery("美国 OR 台湾").field("name").field("address").defaultOperator(Operator.OR);

        searchSourceBuilder.query(query);

        searchRequest.source(searchSourceBuilder);

        //查询获取查询结果
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse));

        //获取命中对象
        SearchHits searchHits = searchResponse.getHits();
        long totalNum = searchHits.getTotalHits().value;
        System.out.println("总记录数："+totalNum);

        List<Person> personList = new ArrayList<>();
        //获取命中的hits数据,搜索结果数据
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit searchHit : hits){
            //获取json字符串格式的数据
            String sourceAsString = searchHit.getSourceAsString();
            Person person = JSON.parseObject(sourceAsString, Person.class);
            personList.add(person);
        }

        System.out.println(JSON.toJSONString(personList));
    }
}
