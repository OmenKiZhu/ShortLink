package com.OmenKi.shortlink.project.controller;

import com.OmenKi.shortlink.project.common.convention.result.Result;
import com.OmenKi.shortlink.project.common.convention.result.Results;
import com.OmenKi.shortlink.project.dto.req.ShortLinkGroupStatsReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkStatsRespDTO;
import com.OmenKi.shortlink.project.service.ShortLinkStatsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/10
 * @Description: 短链接监控控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {
    private final ShortLinkStatsService shortLinkStatsService;
    /**
     * 访问单个短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/v1/stats")
    public Result<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return Results.success(shortLinkStatsService.oneShortLinkStats(requestParam));
    }

    /**
     * 访问单个短链接指定时间内访问记录的监控数据
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/v1/access-record")
    public Result<IPage<ShortLinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
        return Results.success(shortLinkStatsService.shortLinkStatsAccessRecord(requestParam));
    }

    /**
     * 访问分组短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/v1/stats/group")
    public Result<ShortLinkStatsRespDTO> groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam) {
        return Results.success(shortLinkStatsService.groupShortLinkStats(requestParam));
    }
}
