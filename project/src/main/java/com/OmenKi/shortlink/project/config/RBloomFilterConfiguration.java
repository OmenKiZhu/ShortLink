package com.OmenKi.shortlink.project.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/4
 * @Description: 布隆过滤器配置
 */

@Configuration
public class RBloomFilterConfiguration {

    /**
     * 防止短连接创建查询数据库的布隆过滤器
     */
    @Bean
    public RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("userRegisterCachePenetrationBloomFilter");
        cachePenetrationBloomFilter.tryInit(100000000L, 0.001); //两个参数 1.预计加入布隆的元素的数量 2，运行的误判率
        return cachePenetrationBloomFilter;
    }
}