package com.weiyang.trade.lightning.deal;

import com.weiyang.trade.lightning.deal.utils.RedisWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisWorker redisWorker;

    @Test
    void setValue() {
        redisWorker.setValue("test", "test");
    }

    @Test
    void getValue() {
        System.out.println(redisWorker.getValueByKey("test"));
    }

    @Test
    void setActivityIdTest() {
        redisWorker.setValue("ActivityId: 4", 29L);
    }

    @Test
    void activityStockCheckTest() {
        redisWorker.stockDeductCheck("ActivityId: 4");
        System.out.println(redisWorker.getValueByKey("ActivityId: 4"));
    }

    @Test
    void setJmeterStockTest() {
        redisWorker.setValue("ActivityId: 4", 10L);
    }
}
