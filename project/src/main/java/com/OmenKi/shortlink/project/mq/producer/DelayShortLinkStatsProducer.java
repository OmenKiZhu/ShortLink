package com.OmenKi.shortlink.project.mq.producer;

import com.OmenKi.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.OmenKi.shortlink.project.common.constants.RedisKeyConstant.DELAY_QUEUE_STATS_KEY;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/13
 * @Description: 延迟消费短链接统计发送者组件
 */
@Component
@RequiredArgsConstructor
public class DelayShortLinkStatsProducer {
    private final RedissonClient redissonClient;

    /**
     * 发送延迟消费短链接统计
     * 用于将短链接统计记录 (ShortLinkStatsRecordDTO) 以延迟的方式发送到 Redis 中进行后续处理
     * @param statsRecord 短链接统计实体参数
     */
    public void send(ShortLinkStatsRecordDTO statsRecord) {
        // 从Redisson客户端获取或创建一个名为 DELAY_QUEUE_STATS_KEY 的阻塞双端队列（BlockingDeque）。
        RBlockingDeque<ShortLinkStatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(DELAY_QUEUE_STATS_KEY);

        // 获取或创建一个基于上述 BlockingDeque 的延迟队列（DelayedQueue）。
        RDelayedQueue<ShortLinkStatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);

        //将 statsRecord 放入延迟队列中，并设置延迟时间为5秒，后可被消费者取出进行处理
        delayedQueue.offer(statsRecord, 5, TimeUnit.SECONDS);
    }
}
