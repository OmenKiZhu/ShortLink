package com.OmenKi.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/16
 * @Description: 分组持久化对象
 */
@Data
@TableName("t_group")
public class GroupDO {
    /**
     * id
     */
    private Long id;

    /**
     * 短链接分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创建分组的用户名
     */
    private String username;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改时间
     */
    private Date updatetime;

    /**
     * 删除标识
     */
    private Integer delFlag;

}
