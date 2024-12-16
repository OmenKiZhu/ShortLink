package com.OmenKi.shortlink.admin.service;

import com.OmenKi.shortlink.admin.dao.entity.GroupDO;
import com.OmenKi.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.OmenKi.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    /**
     * 查询用户短链接分组集合
     * @return
     */
    List<ShortLinkGroupRespDTO> listGroup();

    /**
     * 修改短链接分组名称
     * @param  requestParam
     */
    void updateGroup(ShortLinkGroupUpdateReqDTO requestParam);

    /**
     * 删除短链接分组
     * @param gid
     */
    void deleteGroup(String gid);
}
