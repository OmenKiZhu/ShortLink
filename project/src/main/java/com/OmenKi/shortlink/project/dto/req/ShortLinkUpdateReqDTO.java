package com.OmenKi.shortlink.project.dto.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/8
 * @Description:
 */
@Data
public class ShortLinkUpdateReqDTO {


    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 想要修改后的分组标识
     */
    private String gid;

    /**
     * 原始分组标识
     */
    private String originGid;


    /**
     * 有效期类型 0：永久有效 1：自定义有效
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String descriptionAlias;
}
