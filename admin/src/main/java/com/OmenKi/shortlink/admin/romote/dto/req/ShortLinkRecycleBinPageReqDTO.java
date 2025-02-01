package com.OmenKi.shortlink.admin.romote.dto.req;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/1
 * @Description: 回收站分页查询短链接请求对象
 */
@Data
public class ShortLinkRecycleBinPageReqDTO extends Page {
    private List<String> gidList;
}
