package com.OmenKi.shortlink.admin.service.impl;

import com.OmenKi.shortlink.admin.dao.entity.GroupDO;
import com.OmenKi.shortlink.admin.dao.mapper.GroupMapper;
import com.OmenKi.shortlink.admin.service.GroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/16
 * @Description: 短链接分组接口实现层
 */
@Service
@Slf4j

public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
}
