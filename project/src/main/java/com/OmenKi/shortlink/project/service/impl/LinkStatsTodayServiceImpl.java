package com.OmenKi.shortlink.project.service.impl;

import com.OmenKi.shortlink.project.dao.entity.LinkStatsTodayDO;
import com.OmenKi.shortlink.project.dao.mapper.LinkStatsTodayMapper;
import com.OmenKi.shortlink.project.service.LinkStatsTodayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/13
 * @Description: 短链接今日统计接口实现层
 */
@Service
public class LinkStatsTodayServiceImpl extends ServiceImpl<LinkStatsTodayMapper, LinkStatsTodayDO> implements LinkStatsTodayService {
}
