package com.OmenKi.shortlink.admin.toolkit;

import java.security.SecureRandom;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/16
 * @Description: 分组Id随机生成器工具
 */
public final class RandomStringGenerator {
    // 定义字符集为数字0-9和字母A-Z, a-z
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();


    public static String generateRandom() {
       return generateRandom(6);
    }
    /**
     * 生成一个指定长度的随机字符串，该字符串由数字和大小写字母组成。
     *
     * @param length 输出字符串的长度
     * @return 长度为length的随机字符串
     */
    public static String generateRandom(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
