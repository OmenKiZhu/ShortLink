package com.OmenKi.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/18
 * @Description: 分组排序请求参数
 */
@Data
public class ShortLinkGroupSortReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 排序字段
     */
    private Integer sortOrder;
}
