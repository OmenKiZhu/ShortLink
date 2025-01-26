package com.OmenKi.shortlink.project.service;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/26
 * @Description: 标题接口层
 */
public interface UrlTitleService {
    /**
     * 根据Url获取标题信息
     * @param url
     * @return
     */
    String getTitleByUrl(String url);
}
