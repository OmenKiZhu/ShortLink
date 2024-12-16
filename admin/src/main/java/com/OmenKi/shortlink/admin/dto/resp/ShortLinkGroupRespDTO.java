package com.OmenKi.shortlink.admin.dto.resp;

import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/16
 * @Description: 短链接分组返回实体
 */

@Data
public class ShortLinkGroupRespDTO {
    /**
     * 短链接分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创建分组的用户名
     */
    private String username;

    /**
     * 分组排序
     */
    private Integer sortOrder;
}
