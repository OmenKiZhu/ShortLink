package com.OmenKi.shortlink.project.common.constants;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/25
 * @Description: 短连接常量类
 */
public class ShortLinkConstant {
    /**
     * 永久短链接默认的缓存有效期
     */

    //默认有效期
    public static final Long DEFAULT_CACHE_VALID_TIME = 2626560000L;

    /**
     * 高德获取地区接口地址前缀路径  （https://restapi.amap.com/v3/ip?parameters [GET]）
     */
    public static final String AMAP_REMOTE_URL = "https://restapi.amap.com/v3/ip";
}
