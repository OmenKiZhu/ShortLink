package com.omenki.shortlink.aggregation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * 短链接聚合服务
 */


@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(value = {
        "com.OmenKi.shortlink.project.dao.mapper",
        "com.OmenKi.shortlink.admin.dao.mapper"
})
public class AggregationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AggregationServiceApplication.class, args);
    }
}