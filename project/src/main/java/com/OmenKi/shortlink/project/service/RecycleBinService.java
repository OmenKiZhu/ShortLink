package com.OmenKi.shortlink.project.service;

import com.OmenKi.shortlink.project.dao.entity.ShortLinkDO;
import com.OmenKi.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/31
 * @Description: 回收站接口层
 */
public interface RecycleBinService extends IService<ShortLinkDO> {
    /**
     * 保存回收站
     * @param requestParams
     */
    void saveRecycleBin(RecycleBinSaveReqDTO requestParams);
}
