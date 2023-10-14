package com.weiyang.trade.lightning.deal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = {"com.weiyang"})
@MapperScan({"com.weiyang.trade.lightning.deal.db.mappers"})
public class TradeLightningDealApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeLightningDealApplication.class, args);
    }

}
