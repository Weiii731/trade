package com.weiyang.trade.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.weiyang"})
@MapperScan({"com.weiyang.trade.goods.db.mappers"})
@SpringBootApplication
public class TradeGoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeGoodsApplication.class, args);
	}

}
