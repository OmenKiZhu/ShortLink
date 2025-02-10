package com.OmenKi.shortlink.project.dao.mapper;

import com.OmenKi.shortlink.project.dao.entity.LinkLocaleStatsDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/10
 * @Description: 地区统计访问持久层
 */
public interface LinkLocaleStatsMapper extends BaseMapper<LinkLocaleStatsDO> {
    /**
     * 监控地区访问监控数据
     * @param linkLocaleStatsDO
     */
    @Insert("INSERT INTO t_link_locale_stats ("
            + "  full_short_url, gid, date, cnt, country, province, city, adcode, create_time, update_time, del_flag"
            + ") VALUES ("
            + "  #{linkLocaleStats.fullShortUrl}, #{linkLocaleStats.gid}, #{linkLocaleStats.date}, "
            + "  #{linkLocaleStats.cnt}, #{linkLocaleStats.country}, #{linkLocaleStats.province},#{linkLocaleStats.city}, #{linkLocaleStats.adcode}, NOW(), NOW(), 0"
            + ") ON DUPLICATE KEY UPDATE "
            + "  cnt = cnt + #{linkLocaleStats.cnt};")

    void shortLinkScaleStats(@Param("linkLocaleStats") LinkLocaleStatsDO linkLocaleStatsDO);
}
