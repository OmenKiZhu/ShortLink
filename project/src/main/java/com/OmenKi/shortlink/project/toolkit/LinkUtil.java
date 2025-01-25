package com.OmenKi.shortlink.project.toolkit;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.Optional;

import static com.OmenKi.shortlink.project.common.constants.ShortLinkConstant.DEFAULT_CACHE_VALID_TIME;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/25
 * @Description: 短链接工具类
 */
public class LinkUtil {

    /**
     * 获取短连接缓存有效期的时间
     * @param validDate
     * @return 有效期时间戳
     */
    public static Long getShortLinkCacheValidTime(Date validDate) {
        return Optional.ofNullable(validDate)
                .map(each -> DateUtil.between(each, new Date(), DateUnit.MS))
                .orElse(DEFAULT_CACHE_VALID_TIME);//默认有效期是一个月

    }
}
