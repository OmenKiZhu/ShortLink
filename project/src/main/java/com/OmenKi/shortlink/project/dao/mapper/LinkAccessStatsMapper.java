package com.OmenKi.shortlink.project.dao.mapper;

import com.OmenKi.shortlink.project.dao.entity.LinkAccessStatsDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/9
 * @Description: 基础访问持久层
 */
public interface LinkAccessStatsMapper extends BaseMapper<LinkAccessStatsDO> {
    /**
     * 记录基础访问新增语句
     * @param linkAccessStatsDO
     */
    @Insert("INSERT INTO t_link_access_stats ("
            + "  full_short_url, gid, date, pv, uv, uip, hour, weekday, create_time, update_time, del_flag"
            + ") VALUES ("
            + "  #{shortLinkAccessStatsDO.fullShortUrl}, #{shortLinkAccessStatsDO.gid}, #{shortLinkAccessStatsDO.date}, "
            + "  #{shortLinkAccessStatsDO.pv}, #{shortLinkAccessStatsDO.uv}, #{shortLinkAccessStatsDO.uip}, "
            + "  #{shortLinkAccessStatsDO.hour}, #{shortLinkAccessStatsDO.weekday}, NOW(), NOW(), 0"
            + ") ON DUPLICATE KEY UPDATE "
            + "  pv = pv + #{shortLinkAccessStatsDO.pv}, "
            + "  uv = uv + #{shortLinkAccessStatsDO.uv}, "
            + "  uip = uip + #{shortLinkAccessStatsDO.uip}")
    void shortLinkStats(@Param("shortLinkAccessStatsDO") LinkAccessStatsDO linkAccessStatsDO);
}
