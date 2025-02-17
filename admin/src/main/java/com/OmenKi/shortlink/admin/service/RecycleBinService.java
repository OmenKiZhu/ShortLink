package com.OmenKi.shortlink.admin.service;

import com.OmenKi.shortlink.admin.common.convention.result.Result;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkPageRespDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/1
 * @Description: 回收站接口层
 */
public interface RecycleBinService {
    /**
     * 分页查询回收站短链接----转发
     * @param requestParam
     * @return
     */
    Result<Page<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
