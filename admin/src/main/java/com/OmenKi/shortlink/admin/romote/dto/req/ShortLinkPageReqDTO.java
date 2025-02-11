package com.OmenKi.shortlink.admin.romote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/3
 * @Description: 短链接分页请求参数
 */
@Data
public class ShortLinkPageReqDTO extends Page {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 排序标签字段
     */
    private String orderByTag;
}
