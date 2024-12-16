package com.OmenKi.shortlink.admin.dao.entity;

import com.OmenKi.shortlink.admin.common.database.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/11/20
 * @Description: 用户持久层对象
 */
@TableName("t_user")
@Data
public class UserDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 注销时间戳
     */
    private Long deletionTime;



}
