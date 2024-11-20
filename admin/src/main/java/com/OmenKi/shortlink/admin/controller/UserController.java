package com.OmenKi.shortlink.admin.controller;

import com.OmenKi.shortlink.admin.dto.resp.UserRespDTO;
import com.OmenKi.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/11/20
 * @Description: 用户管理层
 */
@RestController // 组合注解 相当于requestBody + controller
@RequiredArgsConstructor
public class UserController {

    //构造器注入方式 ！！！！！！
    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @GetMapping("/api/shortlink/v1/user/{username}")
    public UserRespDTO getUserByUsername(@PathVariable("username") String username){
        return userService.getUserByUsername(username);
    }
}
