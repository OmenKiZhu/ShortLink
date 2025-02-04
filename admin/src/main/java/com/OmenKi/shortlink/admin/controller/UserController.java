package com.OmenKi.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.OmenKi.shortlink.admin.common.convention.result.Result;
import com.OmenKi.shortlink.admin.common.convention.result.Results;
import com.OmenKi.shortlink.admin.dto.req.UserLoginReqDTO;
import com.OmenKi.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.OmenKi.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.OmenKi.shortlink.admin.dto.resp.UserActualRespDTO;
import com.OmenKi.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.OmenKi.shortlink.admin.dto.resp.UserRespDTO;
import com.OmenKi.shortlink.admin.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username){
        Result<UserRespDTO> userRespDTOResult = new Result<UserRespDTO>();
        userRespDTOResult.setCode("0");
        userRespDTOResult.setData(userService.getUserByUsername(username));
        return userRespDTOResult;
    }

    /**
     * 根据用户名查询用户无脱敏信息
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username){
        //  BeanUtil.toBean 方法将返回的对象转换为 UserActualRespDTO 类型
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class));
    }

    /**
     * 查询用户名是否存在
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public Result<Boolean> hasUserName(@RequestParam("username") String username){
        return Results.success(userService.hasUserName(username));
    }

    /**
     * 注册用户
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam){
        userService.register(requestParam);
        return Results.success();
    }

    /**
     * 更新用户信息
     * @param requestParam
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/user")
    public Result<Void> updateUser(@RequestBody UserUpdateReqDTO requestParam){
        userService.update(requestParam);
        return Results.success();
    }

    /**
     * 用户登录
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam){
        UserLoginRespDTO userLoginRespDTO = userService.login(requestParam);
        return Results.success(userLoginRespDTO);
    }

    /**
     * 检查用户是否登录
     * @param username
     * @param token
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/check-login")
    public Result<Boolean> checkLogin(
            @RequestParam @NotBlank(message = "Username cannot be empty") @Size(max = 50, message = "Username is too long") String username,
            @RequestParam @NotBlank(message = "Token cannot be empty") String token
    ){
        return Results.success(userService.checkLogin(username,token));
    }

    /**
     * 用户退出登录
     * @param username
     * @param token
     * @return
     */
    @DeleteMapping("/api/short-link/admin/v1/user/login-out")
    public Result<Void> loginOut(@RequestParam String username,@RequestParam String token){
        userService.loginOut(username,token);
        return Results.success();
    }
}
