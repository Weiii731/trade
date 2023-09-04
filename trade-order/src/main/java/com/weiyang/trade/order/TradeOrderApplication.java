package com.weiyang.trade.order;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableRabbit
@SpringBootApplication
@ComponentScan(basePackages = {"com.weiyang"})
@MapperScan({"com.weiyang.trade.order.db.mappers"})
public class TradeOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeOrderApplication.class, args);
	}

}
