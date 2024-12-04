package com.OmenKi.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.OmenKi.shortlink.admin.common.convention.exception.ClientException;
import com.OmenKi.shortlink.admin.dao.entity.UserDO;
import com.OmenKi.shortlink.admin.dao.mapper.UserMapper;
import com.OmenKi.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.OmenKi.shortlink.admin.dto.resp.UserRespDTO;
import com.OmenKi.shortlink.admin.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.OmenKi.shortlink.admin.common.constants.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static com.OmenKi.shortlink.admin.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static com.OmenKi.shortlink.admin.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/11/20
 * @Description: 用户接口实现层
 */
@Service
@RequiredArgsConstructor  //构造器方式注入布隆
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean hasUserName(String username) {
//        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
//                .eq(UserDO::getUsername, username);
//        UserDO userDO = baseMapper.selectOne(queryWrapper); // 执行查询并返回匹配的第一条记录
//        return userDO != null;

        //引入布隆后
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO requestParam) {
        if(hasUserName(requestParam.getUsername())){
            throw new ClientException(USER_NAME_EXIST);
        }
        // 通过分布式锁方式防止海量用户恶意触发同一个未注册名字请求
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUsername());
       try {
           if(lock.tryLock()){
               int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
               if (inserted < 1){
                   throw new ClientException(USER_SAVE_ERROR);
               }

               //布隆过滤器加入名字
               userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
               return;
           }
            throw new ClientException(USER_NAME_EXIST);
       } finally {
           lock.unlock();
       }

    }
}
