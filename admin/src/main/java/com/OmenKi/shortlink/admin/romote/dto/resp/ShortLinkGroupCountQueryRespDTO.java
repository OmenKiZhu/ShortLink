package com.OmenKi.shortlink.admin.romote.dto.resp;

import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/8
 * @Description: 短链接分组查询返回参数
 */
@Data
public class ShortLinkGroupCountQueryRespDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 查询的一个短链接分组下短链接的数量
     */
    private Integer shortLinkCount;
}
