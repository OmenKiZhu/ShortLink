package com.OmenKi.shortlink.project.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/10
 * @Description: 短链接监控访问记录响应参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsAccessRecordRespDTO {

    /**
     * 访客类型
     */
    private String uvType;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

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

    /**
     * 用户信息
     */
    private String user;


    /**
     * 访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private Date createTime;
}
