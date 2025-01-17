package com.OmenKi.shortlink.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/4
 * @Description: 布隆过滤器配置
 */

@Slf4j
@Configuration
public class RBloomFilterConfiguration {

    @Value("${spring.bloom.filter.expected-elements}")
    private Long expectedElements;

    @Value("${spring.bloom.filter.false-positive-probability}")
    private Double falsePositiveProbability;
    /**
     * 防止用户注册不必要的查询数据库的布隆过滤器
     */
    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(RedissonClient redissonClient) {
        //布隆过滤器泛型为String 取决于我要检查的元素类型 这里是为了检查用户名 所以为String
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("userRegisterCachePenetrationBloomFilter");


        if (!cachePenetrationBloomFilter.isExists()) {
            cachePenetrationBloomFilter.tryInit(expectedElements, falsePositiveProbability); //两个参数 1.预计加入布隆的元素的数量 2，运行的误判率
            log.info("布隆过滤器初始化成功, 初始参数为 expectedElements:{}, falsePositiveProbability:{}",expectedElements, falsePositiveProbability);
        }else {
            log.info("布隆过滤器初始化失败，已经存在了哈！！");
        }

        return cachePenetrationBloomFilter;
    }
}