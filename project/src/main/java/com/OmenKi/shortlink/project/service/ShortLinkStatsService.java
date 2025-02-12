package com.OmenKi.shortlink.project.service;

import com.OmenKi.shortlink.project.dto.req.ShortLinkGroupStatsReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkStatsRespDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

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

    /**
     * 访问单个短链接指定时间内访问记录的监控数据
     * @param requestParam
     * @return
     */
    IPage<ShortLinkStatsAccessRecordRespDTO> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam);

    /**
     * 获取分组短链接监控数据
     *
     * @param requestParam 获取分组短链接监控数据入参
     * @return 分组短链接监控数据
     */
    ShortLinkStatsRespDTO groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam);
}
