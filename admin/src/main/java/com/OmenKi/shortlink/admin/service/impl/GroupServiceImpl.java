package com.OmenKi.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.OmenKi.shortlink.admin.common.biz.user.UserContext;
import com.OmenKi.shortlink.admin.common.convention.exception.ClientException;
import com.OmenKi.shortlink.admin.common.convention.result.Result;
import com.OmenKi.shortlink.admin.dao.entity.GroupDO;
import com.OmenKi.shortlink.admin.dao.mapper.GroupMapper;
import com.OmenKi.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.OmenKi.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.OmenKi.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.OmenKi.shortlink.admin.romote.ShortLinkRemoteService;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.OmenKi.shortlink.admin.service.GroupService;
import com.OmenKi.shortlink.admin.toolkit.RandomStringGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.OmenKi.shortlink.admin.common.constants.RedisCacheConstant.LOCK_GROUP_CREATE_KEY;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/16
 * @Description: 短链接分组接口实现层
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    private final RedissonClient redissonClient;

    @Value("${short-link.group.max-num}")
    private Integer groupMaxNum;

    //TODO 后续重构为SpringCloud Feign调用
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService(){

    };
    @Override
    public void saveGroup(String groupName) {
        saveGroup(UserContext.getUsername(), groupName);
    }

    @Override
    public void saveGroup(String username, String groupName) {
        RLock lock = redissonClient.getLock(String.format(LOCK_GROUP_CREATE_KEY, username));
        lock.lock();
        try {
            LambdaQueryWrapper<GroupDO> lambdaQueryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                    .eq(GroupDO::getUsername, username)
                    .eq(GroupDO::getDelFlag, 0);
            List<GroupDO> groupDOList = baseMapper.selectList(lambdaQueryWrapper);
            if(CollUtil.isNotEmpty(groupDOList) && groupDOList.size() == groupMaxNum) {
                throw new ClientException(String.format("已超出最大分组数：%d", groupMaxNum));
            }
            String gid;
            do {
                //随机生成6位gid
                gid = RandomStringGenerator.generateRandom();
            } while (hasGid(username,gid)); //直至生成之前不存在的gid
            //插入一个新的分组
            GroupDO groupDO = GroupDO.builder()
                    .gid(gid)
                    .sortOrder(0)
                    .name(groupName)
                    .username(username)
                    .build();
            baseMapper.insert(groupDO);
        }finally{
            lock.unlock();;
        }
    }

    @Override
    public List<ShortLinkGroupRespDTO>listGroup() {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);

        List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);
        Result<List<ShortLinkGroupCountQueryRespDTO>> listResult = shortLinkRemoteService
                .listGroupShortLinkCount(groupDOList.stream().map(GroupDO::getGid).toList());
        List<ShortLinkGroupRespDTO> shortLinkGroupRespDTOList = BeanUtil.copyToList(groupDOList, ShortLinkGroupRespDTO.class);
        shortLinkGroupRespDTOList.forEach(each -> {
            String gid = each.getGid();
            Optional<ShortLinkGroupCountQueryRespDTO> first = listResult.getData().stream().filter(item -> Objects.equals(item.getGid(), each.getGid()))
                    .findFirst();
            first.ifPresent(item -> each.setShortLinkCount(first.get().getShortLinkCount()));
        });
        return shortLinkGroupRespDTOList;
    }

    @Override
    public void updateGroup(ShortLinkGroupUpdateReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> updateWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, requestParam.getGid())
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = new GroupDO();
        groupDO.setName(requestParam.getName());
        int update = baseMapper.update(groupDO, updateWrapper);
    }

    @Override
    public void deleteGroup(String gid) {
        LambdaQueryWrapper<GroupDO> updateWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = new GroupDO();
        groupDO.setDelFlag(1);
        int update = baseMapper.update(groupDO, updateWrapper);
    }

    @Override
    public void SortGroup(List<ShortLinkGroupSortReqDTO> requestParam) {
        requestParam.forEach(each -> {
            GroupDO groupDO = GroupDO.builder()
                    .gid(each.getGid())
                    .sortOrder(each.getSortOrder())
                    .build();
            LambdaQueryWrapper<GroupDO> updateWrapper = Wrappers.lambdaQuery(GroupDO.class)
                    .eq(GroupDO::getUsername, UserContext.getUsername())
                    .eq(GroupDO::getGid, each.getGid())
                    .eq(GroupDO::getDelFlag, 0);
            baseMapper.update(groupDO,updateWrapper);
        });
    }


    private boolean hasGid(String username, String gid) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername, Optional.ofNullable(username).orElse(UserContext.getUsername()));
        GroupDO hasGroupFlag = baseMapper.selectOne(queryWrapper);
        return hasGroupFlag != null;
    }
}
