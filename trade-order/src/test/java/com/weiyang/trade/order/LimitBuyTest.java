package com.weiyang.trade.order;

import com.weiyang.trade.order.service.LimitBuyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LimitBuyTest {
    @Autowired
    public LimitBuyService limitBuyService;

    @Test
    public void addLimitMemberTest() {
        limitBuyService.addLimitMember(123456L, 1234L);
    }

    @Test
    public void isInLimitMemberTest() {
        limitBuyService.isInLimitMember(123456L, 668899L);
    }

    @Test
    public void removeLimitMemberTest() {
        limitBuyService.removeLimitMember(123456L, 1234L);
    }
}
