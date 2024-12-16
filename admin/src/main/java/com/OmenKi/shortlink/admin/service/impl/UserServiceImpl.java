package com.OmenKi.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.OmenKi.shortlink.admin.common.convention.exception.ClientException;
import com.OmenKi.shortlink.admin.dao.entity.UserDO;
import com.OmenKi.shortlink.admin.dao.mapper.UserMapper;
import com.OmenKi.shortlink.admin.dto.req.UserLoginReqDTO;
import com.OmenKi.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.OmenKi.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.OmenKi.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.OmenKi.shortlink.admin.dto.resp.UserRespDTO;
import com.OmenKi.shortlink.admin.service.UserService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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
    private final StringRedisTemplate stringRedisTemplate;
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

    @Override
    public void update(UserUpdateReqDTO requestParam) {
        // TODO 验证当前登录的用户名跟请求的用户名是否一致
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class),updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        // 1. 首先验证用户名和密码是否存在数据库
        LambdaQueryWrapper<UserDO> loginWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO userDO = baseMapper.selectOne(loginWrapper);
        if(userDO == null) {
            throw new ClientException("用户不存在");
        }
        Boolean hasLogin = stringRedisTemplate.hasKey("login_" + userDO.getUsername());
        if(hasLogin != null && hasLogin){
            throw new ClientException("用户已登录");
        }

        /**
         * Hash:
         * Key: login_用户名
         * Value:
         *  key: token标识
         *  val: JSON字符串（用户信息）
         */
        // 2.可以查到 存储到redis
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put("login_" + userDO.getUsername(), uuid, JSON.toJSONString(userDO));
        stringRedisTemplate.expire("login_" + userDO.getUsername(),30L, TimeUnit.DAYS);
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String username, String token) {
        return stringRedisTemplate.opsForHash().get("login_" + username, token) != null;
    }

    @Override
    public void loginOut(String username, String token) {
        if (checkLogin(username, token))
        {
            stringRedisTemplate.delete("login_" + username);
            return;
        }
        throw new ClientException("用户Token不存在或者未登录");
    }
}
