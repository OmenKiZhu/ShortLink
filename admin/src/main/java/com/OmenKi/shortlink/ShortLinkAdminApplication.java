package com.OmenKi.shortlink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/11/14
 * @Description:
 */
@EnableDiscoveryClient
@EnableFeignClients("com.OmenKi.shortlink.admin.romote")
@SpringBootApplication
@MapperScan("com.OmenKi.shortlink.admin.dao.mapper")
public class ShortLinkAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortLinkAdminApplication.class, args);
    }
}
