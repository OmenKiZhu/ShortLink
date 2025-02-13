package com.OmenKi.shortlink.project.mq.consumer;

import com.OmenKi.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.OmenKi.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import static com.OmenKi.shortlink.project.common.constants.RedisKeyConstant.DELAY_QUEUE_STATS_KEY;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/13
 * @Description: 延迟记录短链接统计组件
 */
@Component
@RequiredArgsConstructor
public class DelayShortLinkStatsConsumer implements InitializingBean {
    private final RedissonClient redissonClient;
    private final ShortLinkService shortLinkService;

    public void onMessage() {
        // 创建一个新的单线程执行器，并通过匿名内部类自定义线程属性
        Executors.newSingleThreadExecutor(


                        // runnable这部分作为参数传给newSingleThreadExecutor的重载部分
                        // Lambda 表达式的左边 (runnable) 是传入的 Runnable 实例
                        // 右边的大括号 { ... } 包含了实际的逻辑来创建和配置一个新的 Thread 对象。
                        runnable -> {
                            //使用传入的 Runnable 创建一个新的 Thread 实例
                            Thread thread = new Thread(runnable);

                            //设置新创建线程的名字为 "delay_short-link_stats_consumer"
                            thread.setName("delay_short-link_stats_consumer");

                            //将线程设置为守护线程（daemon thread）。
                            // 守护线程是一种特殊类型的线程，当所有非守护线程结束时，即使守护线程仍在运行，JVM 也会退出。通常用于后台任务，如垃圾回收等。
                            thread.setDaemon(Boolean.TRUE);
                            return thread;
                        })
                .execute(() -> {
                    // 在执行器中，从 Redis 获取阻塞 双端队列 和 延迟队列。
                    RBlockingDeque<ShortLinkStatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(DELAY_QUEUE_STATS_KEY);
                    RDelayedQueue<ShortLinkStatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);



                    for (; ; ) {
                        try {
                            // 在无限循环中，尝试从延迟队列中获取到期的消息（ShortLinkStatsRecordDTO）
                            ShortLinkStatsRecordDTO statsRecord = delayedQueue.poll();
                            if (statsRecord != null) {
                                // 如果获取到，则调用 shortLinkService.shortLinkStats 处理这条消息
                                shortLinkService.shortLinkStats(null, null, statsRecord);
                                continue;
                            }
                            // 如果没有消息，则让当前线程等待500毫秒
                            LockSupport.parkUntil(500);
                        } catch (Throwable ignored) {
                        }
                    }
                });
    }

    //实现了 InitializingBean 接口的方法，在所有属性设置完成后调用 onMessage() 方法启动消费者。
    @Override
    public void afterPropertiesSet() throws Exception {
        onMessage();
    }
}
