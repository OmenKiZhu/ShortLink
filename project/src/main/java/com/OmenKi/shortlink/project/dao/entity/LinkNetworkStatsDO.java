package com.OmenKi.shortlink.project.dao.entity;

import com.OmenKi.shortlink.project.common.database.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/10
 * @Description: 监测网络类型实体类
 */
@Data
@TableName("t_link_network_stats")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkNetworkStatsDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 日期
     */
    private Date date;

    /**
     * 访问量
     */
    private Integer cnt;

    /**
     * 访问网络
     */
    private String network;
}
