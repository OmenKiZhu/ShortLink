package com.OmenKi.shortlink.admin.controller;

import com.OmenKi.shortlink.admin.common.convention.result.Result;
import com.OmenKi.shortlink.admin.common.convention.result.Results;
import com.OmenKi.shortlink.admin.dto.req.RecycleBinRecoverReqDTO;
import com.OmenKi.shortlink.admin.dto.req.RecycleBinSaveReqDTO;
import com.OmenKi.shortlink.admin.romote.ShortLinkRemoteService;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkPageRespDTO;
import com.OmenKi.shortlink.admin.service.RecycleBinService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/31
 * @Description: 回收站控制层
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {
    private final RecycleBinService recycleBinService;

    //TODO 后续重构为SpringCloud Feign调用
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService(){

    };

    /**
     * 保存回收站
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        shortLinkRemoteService.saveRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 分页查询回收站短链接
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        return recycleBinService.pageRecycleBinShortLink(requestParam);
    }

    /**
     * 回收站恢复功能
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        shortLinkRemoteService.recoverRecycleBin(requestParam);
        return Results.success();
    }
}
