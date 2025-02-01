package com.OmenKi.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/31
 * @Description: 回收站恢复入参
 */
@Data
public class RecycleBinRemoveReqDTO {
    private String gid;

    /**
     * 全部短链接
     */
    private String fullShortUrl;
}
