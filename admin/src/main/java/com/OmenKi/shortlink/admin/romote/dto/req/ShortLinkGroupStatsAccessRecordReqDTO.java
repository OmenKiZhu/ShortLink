package com.OmenKi.shortlink.admin.romote.dto.req;

import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/12
 * @Description: 分组短链接监控访问记录请求参数
 */
@Data
public class ShortLinkGroupStatsAccessRecordReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;
}
