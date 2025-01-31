package com.OmenKi.shortlink.project.service.impl;

import com.OmenKi.shortlink.project.dao.entity.ShortLinkDO;
import com.OmenKi.shortlink.project.dao.mapper.ShortLinkMapper;
import com.OmenKi.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import com.OmenKi.shortlink.project.service.RecycleBinService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.OmenKi.shortlink.project.common.constants.RedisKeyConstant.GOTO_SHORT_LINK_KEY;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/31
 * @Description: 回收站接口实现类
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements RecycleBinService {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveRecycleBin(RecycleBinSaveReqDTO requestParams) {
        LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                .eq(ShortLinkDO::getFullShortUrl, requestParams.getFullShortUrl())
                .eq(ShortLinkDO::getGid, requestParams.getGid())
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0);

        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .enableStatus(1)
                .build();
        baseMapper.update(shortLinkDO,updateWrapper);

        //删除一下原来的缓存
        stringRedisTemplate.delete(String.format(GOTO_SHORT_LINK_KEY, requestParams.getFullShortUrl()));
    }
}