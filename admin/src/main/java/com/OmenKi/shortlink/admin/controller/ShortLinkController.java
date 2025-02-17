package com.OmenKi.shortlink.admin.controller;

import com.OmenKi.shortlink.admin.common.convention.result.Result;
import com.OmenKi.shortlink.admin.common.convention.result.Results;
import com.OmenKi.shortlink.admin.romote.ShortLinkActualRemoteService;
import com.OmenKi.shortlink.admin.romote.ShortLinkRemoteService;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkBatchCreateReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkCreateReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkPageReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkUpdateReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkBaseInfoRespDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkBatchCreateRespDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkCreateRespDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkPageRespDTO;
import com.OmenKi.shortlink.admin.toolkit.EasyExcelWebUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/7
 * @Description:
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ShortLinkController {
    private final ShortLinkActualRemoteService shortLinkActualRemoteService;
    //TODO 后续重构为SpringCloud Feign调用
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService(){};


    /**
     * 创建短链接
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkActualRemoteService.createShortLink(requestParam);
    }
    /**
     * 分页查询短链接
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return shortLinkActualRemoteService.pageShortLink(requestParam.getGid(), requestParam.getOrderTag(), requestParam.getCurrent(), requestParam.getSize());
    }

    /**
     * 更新短链接信息
     * @param requestParam
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkActualRemoteService.updateShortLink(requestParam);
        return Results.success();
    }

    /**
     * 批量创建短链接
     */
    @SneakyThrows
    @PostMapping("/api/short-link/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        Result<ShortLinkBatchCreateRespDTO> shortLinkBatchCreateRespDTOResult = shortLinkActualRemoteService.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortLinkBaseInfoRespDTO> baseLinkInfos = shortLinkBatchCreateRespDTOResult.getData().getBaseLinkInfos();
            log.info("批量短链接进行中-------------------------");
            EasyExcelWebUtil.write(response, "批量创建短链接-SaaS短链接系统", ShortLinkBaseInfoRespDTO.class, baseLinkInfos);
            log.info("批量短链接excel文件已经下载完成-------------");
        }
    }

}
