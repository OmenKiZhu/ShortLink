package com.OmenKi.shortlink.project.service;

import com.OmenKi.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkStatsRespDTO;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/10
 * @Description: 短链接监控接口层
 */
public interface ShortLinkStatsService {
    /**
     * 获取单个短链接监控数据
     *
     * @param requestParam 获取短链接监控数据入参
     * @return 短链接监控数据
     */
    ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam);
}
