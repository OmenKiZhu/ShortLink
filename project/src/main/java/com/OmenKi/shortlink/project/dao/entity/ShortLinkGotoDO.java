package com.OmenKi.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/23
 * @Description:
 */
@TableName("t_link_goto")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkGotoDO {

    /**
     * id
     */
    private Long id;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 完整短连接
     */
    private String fullShortUrl;
}
