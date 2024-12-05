package com.OmenKi.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/5
 * @Description: 用户登录请求参数
 */
@Data
public class UserLoginReqDTO {

    private String username;

    private String password;
}
