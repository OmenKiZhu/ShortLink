package com.OmenKi.shortlink.admin.toolkit;/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/17
 * @Description: 邮箱脱敏工具类
 */public class DesensitizedUtil {
    /**
     * 对邮箱地址进行脱敏处理，默认保留第一个字符和域名。
     *
     * @param email 待脱敏的邮箱地址
     * @return 脱敏后的邮箱地址
     */
    public static String emailWithFirstChar(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "";
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex >= email.length() - 1) {
            // 如果没有有效的 '@' 符号，直接返回原字符串
            return email;
        }

        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if (username.length() <= 1) {
            // 用户名长度小于等于1时，不进行脱敏
            return email;
        }

        // 默认保留第一个字符，其余用 * 替换
        return username.charAt(0) + "****" + domain;
    }

    /**
     * 对邮箱地址进行脱敏处理，保留前两个字符和域名。
     *
     * @param email 待脱敏的邮箱地址
     * @return 脱敏后的邮箱地址
     */
    public static String emailWithTwoChars(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "";
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex >= email.length() - 1) {
            // 如果没有有效的 '@' 符号，直接返回原字符串
            return email;
        }

        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if (username.length() <= 2) {
            // 用户名长度小于等于2时，不进行脱敏
            return email;
        }

        // 保留前两个字符，其余用 * 替换
        return username.substring(0, 2) + "*****" + domain;
    }

    /**
     * 对邮箱地址进行脱敏处理，保留首尾字符和域名。
     *
     * @param email 待脱敏的邮箱地址
     * @return 脱敏后的邮箱地址
     */
    public static String emailWithFirstAndLastChar(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "";
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex >= email.length() - 1) {
            // 如果没有有效的 '@' 符号，直接返回原字符串
            return email;
        }

        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if (username.length() <= 2) {
            // 用户名长度小于等于2时，不进行脱敏
            return email;
        }

        // 保留首尾字符，其余用 * 替换
        return username.charAt(0) + "*********" + username.charAt(username.length() - 1) + domain;
    }
}
