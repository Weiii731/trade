package com.weiyang.trade.goods.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class GoodsConrtoller {

    @RequestMapping("/good/test")
    @ResponseBody
    public String test() {
        return "Hello, world";
    }
}
