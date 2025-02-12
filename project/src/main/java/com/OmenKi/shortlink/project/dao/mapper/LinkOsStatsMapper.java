package com.OmenKi.shortlink.project.dao.mapper;

import com.OmenKi.shortlink.project.dao.entity.LinkOsStatsDO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/10
 * @Description: 操作系统获取的持久层
 */
public interface LinkOsStatsMapper extends BaseMapper<LinkOsStatsDO> {

    @Insert("INSERT INTO t_link_os_stats ("
            + "  full_short_url, gid, date, cnt, os, create_time, update_time, del_flag"
            + ") VALUES ("
            + "  #{LinkOsStats.fullShortUrl}, #{LinkOsStats.gid}, #{LinkOsStats.date}, "
            + "  #{LinkOsStats.cnt},  #{LinkOsStats.os}, NOW(), NOW(), 0"
            + ") ON DUPLICATE KEY UPDATE "
            + "  cnt = cnt + #{LinkOsStats.cnt};")
    void shortLinkOsStats(@Param("LinkOsStats") LinkOsStatsDO linkOsStatsDO);

    /**
     * 根据短链接获取指定日期内操作系统监控数据
     */
    @Select("SELECT " +
            "    os, " +
            "    SUM(cnt) AS count " +
            "FROM " +
            "    t_link_os_stats " +
            "WHERE " +
            "    full_short_url = #{param.fullShortUrl} " +
            "    AND gid = #{param.gid} " +
            "    AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    full_short_url, gid, date, os;")
    List<HashMap<String, Object>> listOsStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
}
