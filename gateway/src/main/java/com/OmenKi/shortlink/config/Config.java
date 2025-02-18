package com.OmenKi.shortlink.config;

import lombok.Data;

import java.util.List;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/18
 * @Description: 过滤器配置
 */
@Data
public class Config {
    /**
     * 白名单前置路径
     */
    private List<String> whitePathList;
}
