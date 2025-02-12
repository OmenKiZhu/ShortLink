package com.OmenKi.shortlink.project.dao.mapper;

import com.OmenKi.shortlink.project.dao.entity.ShortLinkDO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/30
 * @Description: 短链接持久层
 */
public interface ShortLinkMapper extends BaseMapper<ShortLinkDO> {

    /**
     * 短链接访问统计自增
     * @param gid
     * @param fullShortUrl
     * @param totalPv
     * @param totalUv
     * @param totalUip
     */
    @Update("update t_link set total_pv = total_pv + #{totalPv}, total_uv = total_uv + #{totalUv}, total_uip = total_uip + #{totalUip} where gid = #{gid} and full_short_url = #{fullShortUrl}")
    void incrementStats(
            @Param("gid") String gid,
            @Param("fullShortUrl") String fullShortUrl,
            @Param("totalPv") Integer totalPv,
            @Param("totalUv") Integer totalUv,
            @Param("totalUip") Integer totalUip
    );

    /**
     * 分页统计短链接
     * @param requestParam
     * @return
     */
    IPage<ShortLinkDO> pageLink(ShortLinkPageReqDTO requestParam);
}
