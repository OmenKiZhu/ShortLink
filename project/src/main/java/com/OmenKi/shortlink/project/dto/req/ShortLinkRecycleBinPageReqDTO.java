package com.OmenKi.shortlink.project.dto.req;


import com.OmenKi.shortlink.project.dao.entity.ShortLinkDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/1
 * @Description: 回收站分页查询短链接请求对象
 */
@Data
public class ShortLinkRecycleBinPageReqDTO extends Page<ShortLinkDO> {
    private List<String> gidList;
}
