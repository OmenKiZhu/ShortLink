package com.OmenKi.shortlink.project.service;

import com.OmenKi.shortlink.project.dao.entity.ShortLinkDO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkBatchCreateReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkBatchCreateRespDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/30
 * @Description: 短链接接口层
 */
public interface ShortLinkService extends IService<ShortLinkDO> {




    /**
     * 创建短链接
     * @param requestParam
     * @return
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);

    /**
     * 分页查询短链接
     * @param requestParam
     * @return
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam);

    /**
     * 分组统计短链接
     * @param requestParam
     * @return
     */
    List<ShortLinkGroupCountQueryRespDTO> listShortLinkGroupCount(List<String> requestParam);

    /**
     * 更新短链接信息
     * @param requestParam
     */
    void updateShortLink(ShortLinkUpdateReqDTO requestParam);

    /**
     * 短链接跳转
     * @param shortUri
     * @param request
     * @param response
     */
    void restoreUrl(String shortUri, ServletRequest request, ServletResponse response) throws IOException;

    /**
     * 批量创建短链接
     * @param requestParam
     * @return
     */
    ShortLinkBatchCreateRespDTO batchCreateShortLink(ShortLinkBatchCreateReqDTO requestParam);
}
