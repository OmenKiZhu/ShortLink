package com.OmenKi.shortlink.admin.dto.resp;

import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/4
 * @Description: 真实化的用户对象
 */
@Data
public class UserActualRespDTO {
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
