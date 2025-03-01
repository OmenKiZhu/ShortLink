package com.OmenKi.shortlink.project.dao.entity;

import com.OmenKi.shortlink.project.common.database.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/10
 * @Description: 访问日志监控实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_link_access_logs")
public class LinkAccessLogsDO extends BaseDO {
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
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 用户信息
     */
    private String user;

    /**
     * ip
     */
    private String ip;

    /**
     * 网络类型
     */
    private String network;

    /**
     * 网络设备
     */
    private String device;

    /**
     * 地区
     */
    private String locale;

}
