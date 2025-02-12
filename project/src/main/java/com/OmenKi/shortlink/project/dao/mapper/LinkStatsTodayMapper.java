package com.OmenKi.shortlink.project.dao.mapper;

import com.OmenKi.shortlink.project.dao.entity.LinkStatsTodayDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/11
 * @Description: 短链接今日统计持久层
 */
public interface LinkStatsTodayMapper extends BaseMapper<LinkStatsTodayDO> {
    @Insert("INSERT INTO t_link_stats_today ("
            + "  full_short_url, gid, date, today_pv, today_uv, today_uip , create_time, update_time, del_flag"
            + ") VALUES ("
            + "  #{LinkTodayStats.fullShortUrl}, #{LinkTodayStats.gid}, #{LinkTodayStats.date}, "
            + "  #{LinkTodayStats.todayPv},  #{LinkTodayStats.todayUv}, #{LinkTodayStats.todayUip}, NOW(), NOW(), 0"
            + ") ON DUPLICATE KEY UPDATE "
            + "  today_uv = today_uv + #{LinkTodayStats.todayUv}, today_pv = today_pv + #{LinkTodayStats.todayPv}, today_uip = today_uip + #{LinkTodayStats.todayUip};")
    void shortLinkTodayStats(@Param("LinkTodayStats") LinkStatsTodayDO linkStatsTodayDO);
}
