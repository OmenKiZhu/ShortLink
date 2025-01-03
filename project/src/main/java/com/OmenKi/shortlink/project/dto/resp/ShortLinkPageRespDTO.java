package com.OmenKi.shortlink.project.dto.resp;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/3
 * @Description: 分页返回参数
 */
@Data
public class ShortLinkPageRespDTO {


    /**
     * 域名
     */
    private String domain;

    /**
     * 短链接
     */
    private String shortUri;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 原始链接
     */
    private String originUrl;



    /**
     * 分组标识
     */
    private String gid;



    /**
     * 有效期类型 0：永久有效 1：自定义有效
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    private Date validDate;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String descriptionAlias;

    /**
     * 网站标识
     */
    private String favicon;
}
