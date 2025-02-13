package com.OmenKi.shortlink.admin.common.constants;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/4
 * @Description: Redis 缓存常量命名类
 */
public class RedisCacheConstant {
    /**
     * 用户注册分布式锁
     */
    public static final String LOCK_USER_REGISTER_KEY = "short-link:lock_user-register:";


    /**
     * 分组创建分布式锁
     */
    public static final String LOCK_GROUP_CREATE_KEY = "short-link:lock_group-create:%s";
}
