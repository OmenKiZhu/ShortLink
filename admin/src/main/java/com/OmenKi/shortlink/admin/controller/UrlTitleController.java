package com.OmenKi.shortlink.admin.controller;

import com.OmenKi.shortlink.admin.common.convention.result.Result;
import com.OmenKi.shortlink.admin.romote.ShortLinkRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/26
 * @Description: URL标题控制层
 */
@RestController
@RequiredArgsConstructor
public class UrlTitleController {
    //TODO 后续重构为open feign 调用
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService(){
    };
    /**
     * 根据URL获取标题
     * @param url
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url) {
        return shortLinkRemoteService.getTitleByUrl(url);
    }

}
