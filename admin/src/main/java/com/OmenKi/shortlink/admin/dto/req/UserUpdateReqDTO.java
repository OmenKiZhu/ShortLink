package com.OmenKi.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/5
 * @Description:
 */
@Data
public class UserUpdateReqDTO {
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
}
