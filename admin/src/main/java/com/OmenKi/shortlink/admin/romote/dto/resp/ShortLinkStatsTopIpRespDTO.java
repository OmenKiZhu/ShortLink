package com.OmenKi.shortlink.admin.romote.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/10
 * @Description: 短链接高频访问IP监控响应参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsTopIpRespDTO {

    /**
     * 统计
     */
    private Integer cnt;

    /**
     * IP
     */
    private String ip;
}