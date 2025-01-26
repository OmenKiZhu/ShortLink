package com.OmenKi.shortlink.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/26
 * @Description: 访问页面不存在的控制器
 */
@Controller
public class ShortLinkNotFoundController {
    /**
     * 短链接不存在跳转页面
     */
    @RequestMapping("/page/notfound")
    public String notfound() {
        return "notfound";
    }
}
