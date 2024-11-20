package com.OmenKi.shortlink.admin.dto.resp;

import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/11/20
 * @Description: 用户返回参数响应
 */
@Data
public class UserRespDTO {

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;


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
