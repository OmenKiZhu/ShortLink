package com.OmenKi.shortlink.project.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/8
 * @Description: 有效期类型枚举类
 */
@RequiredArgsConstructor
public enum ValiDateTypeEnum {
    /**
     * 永久有效
     */
    PERMANENT(0),

    /**
     * 自定义有效
     */
    CUSTOM(1);

    @Getter
    private final int type;
}
