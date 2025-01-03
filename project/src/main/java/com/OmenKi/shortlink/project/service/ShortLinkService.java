package com.OmenKi.shortlink.project.service;

import com.OmenKi.shortlink.project.dao.entity.ShortLinkDO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
