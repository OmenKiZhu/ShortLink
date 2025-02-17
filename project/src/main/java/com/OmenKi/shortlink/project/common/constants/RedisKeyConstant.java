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

    public static final String GOTO_SHORT_LINK_KEY = "short-link:goto:%s"; //这里的%s为  域名+短链接

    public static final String GOTO_IS_NULL_SHORT_LINK_KEY = "short-link:is_null:goto:%s"; //这里的%s为  域名+短链接

    /**
     * 短链接跳转锁前缀
     *
     */
    public static final String LOCK_GOTO_SHORT_LINK_KEY = "short-link:lock:goto:%s"; //这里的%s为  域名+短链接

    /**
     * 短链接修改分组 ID 锁前缀 Key
     */
    public static final String LOCK_GID_UPDATE_KEY = "short-link:lock:update-gid:%s";

    /**
     * 短链接延迟队列消费统计 Key
     */
    public static final String DELAY_QUEUE_STATS_KEY = "short-link:delay-queue:stats";

    /**
     * 短链接判断是否新用户缓存标识
     */
    public static final String SHORT_LINK_STATS_UV_KEY = "short-link:stats:uv:";

    /**
     * 短链接判断是否新IP缓存标识
     */
    public static final String SHORT_LINK_STATS_UIP_KEY = "short-link:stats:uip:";

}
