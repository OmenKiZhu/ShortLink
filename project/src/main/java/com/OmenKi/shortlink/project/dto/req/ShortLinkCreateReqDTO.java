package com.OmenKi.shortlink.project.dto.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/30
 * @Description: 短链接创建请求实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortLinkCreateReqDTO {
    /**
     * 域名
     */
    private String domain;

    /**
     * 原始链接
     */
    private String originUrl;



    /**
     * 分组标识
     */
    private String gid;



    /**
     * 创建类型 0：接口创建 1：控制台创建
     */
    private Integer createType;

    /**
     * 有效期类型 0：永久有效 1：自定义有效
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validDate;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String descriptionAlias;
}
