package com.OmenKi.shortlink.project.dto.req;

import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/31
 * @Description:
 */
@Data
public class RecycleBinSaveReqDTO {
    private String gid;

    /**
     * 全部短链接
     */
    private String fullShortUrl;
}
