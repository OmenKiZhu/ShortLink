package com.OmenKi.shortlink.project.controller;

import com.OmenKi.shortlink.project.common.convention.result.Result;
import com.OmenKi.shortlink.project.common.convention.result.Results;
import com.OmenKi.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import com.OmenKi.shortlink.project.dto.req.RecycleBinRemoveReqDTO;
import com.OmenKi.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.OmenKi.shortlink.project.service.RecycleBinService;
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

    /**
     * 短链接添至回收站
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        recycleBinService.saveRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 分页查询回收站短链接
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {

        return Results.success(recycleBinService.pageShortLink(requestParam));
    }

    /**
     * 回收站恢复功能
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        recycleBinService.recoverRecycleBin(requestParam);
        return Results.success();
    }


    /**
     * 回收站删除短链接功能
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/v1/recycle-bin/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        recycleBinService.removeRecycleBin(requestParam);
        return Results.success();
    }
}
