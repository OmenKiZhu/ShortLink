package com.OmenKi.shortlink.admin.service;

import com.OmenKi.shortlink.admin.dao.entity.UserDO;
import com.OmenKi.shortlink.admin.dto.resp.UserRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/11/20
 * @Description: 用户接口层
 */
public interface UserService extends IService<UserDO> {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    UserRespDTO getUserByUsername(String username);
}
