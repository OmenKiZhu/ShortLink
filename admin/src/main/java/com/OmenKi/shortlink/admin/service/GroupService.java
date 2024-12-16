package com.OmenKi.shortlink.admin.service;

import com.OmenKi.shortlink.admin.dao.entity.GroupDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/16
 * @Description: 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 新增短链接分组
     * @param groupName 短链接分组名
     */
    void saveGroup(String groupName);
}
