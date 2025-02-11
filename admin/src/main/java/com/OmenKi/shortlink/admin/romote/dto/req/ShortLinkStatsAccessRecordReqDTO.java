package com.OmenKi.shortlink.admin.romote.dto.req;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/10
 * @Description: 短链接监控访问记录请求参数
 */
@Data
public class ShortLinkStatsAccessRecordReqDTO extends Page {

    //以下是一些查询分页类LinkAccessLogsDO的过滤属性


    /**
     * 完整短链接
     */
    private String fullShortUrl;

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
