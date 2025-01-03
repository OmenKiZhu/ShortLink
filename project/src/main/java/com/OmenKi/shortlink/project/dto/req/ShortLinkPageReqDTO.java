package com.OmenKi.shortlink.project.dto.req;

import com.OmenKi.shortlink.project.dao.entity.ShortLinkDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/3
 * @Description: 短链接分页请求参数
 */
@Data
public class ShortLinkPageReqDTO extends Page<ShortLinkDO> {
    /**
     * 分组标识
     */
    private String gid;
}
