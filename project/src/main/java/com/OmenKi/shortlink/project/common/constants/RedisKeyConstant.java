package com.OmenKi.shortlink.project.common.constants;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/25
 * @Description: Redis常量类
 */
public class RedisKeyConstant {

    /**
     * 短链接跳转redis Key
     *
     */

    public static final String GOTO_SHORT_LINK_KEY = "short-link_goto_%s"; //这里的%s为  域名+短链接

    public static final String GOTO_IS_NULL_SHORT_LINK_KEY = "goto_is_null_short-link_goto_%s"; //这里的%s为  域名+短链接

    /**
     * 短链接跳转锁前缀
     *
     */
    public static final String LOCK_GOTO_SHORT_LINK_KEY = "short-link_lock_goto_%s"; //这里的%s为  域名+短链接

    /**
     * 短链接修改分组 ID 锁前缀 Key
     */
    public static final String LOCK_GID_UPDATE_KEY = "short-link_lock_update-gid_%s";

    /**
     * 短链接延迟队列消费统计 Key
     */
    public static final String DELAY_QUEUE_STATS_KEY = "short-link_delay-queue:stats";
}
