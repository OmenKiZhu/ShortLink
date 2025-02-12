package com.OmenKi.shortlink.admin.toolkit;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/12
 * @Description: 工具类用于解析日期字符串为 ZonedDateTime 对象
 */
public class DateParser {

    /**
     * 解析特定格式的日期字符串到 ZonedDateTime 对象
     *
     * @param dateStr 需要解析的日期字符串
     * @return 解析后的 ZonedDateTime 对象
     * @throws DateTimeParseException 如果解析失败会抛出此异常
     */
    public static ZonedDateTime parseZonedDateTime(String dateStr) throws DateTimeParseException {
        // 定义日期格式
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("EEE MMM dd HH:mm:ss ")
                .appendZoneId() // 使用时区ID解析时区
                .appendPattern(" yyyy")
                .toFormatter(Locale.ENGLISH);

        // 解析日期字符串为 ZonedDateTime
        return ZonedDateTime.parse(dateStr, formatter);
    }

    /**
     * 尝试解析日期字符串并返回结果，如果解析失败则返回 null
     *
     * @param dateStr 需要解析的日期字符串
     * @return 解析后的 ZonedDateTime 对象，如果解析失败则返回 null
     */
    public static ZonedDateTime tryParseZonedDateTime(String dateStr) {
        try {
            return parseZonedDateTime(dateStr);
        } catch (DateTimeParseException e) {
            // 记录错误日志（可选）
            System.err.println("Failed to parse date string: " + dateStr);
            return null;
        }
    }

    // 可以根据需要添加更多方法，比如支持不同格式的解析等
}