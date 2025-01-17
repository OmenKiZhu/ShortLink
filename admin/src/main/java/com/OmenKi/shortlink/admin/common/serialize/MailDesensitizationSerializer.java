package com.OmenKi.shortlink.admin.common.serialize;


import com.OmenKi.shortlink.admin.toolkit.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/17
 * @Description: 邮箱脱敏
 */
public class MailDesensitizationSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String mailDesensitizedUtil = DesensitizedUtil.emailWithFirstChar(s);
        // 将脱敏后的电话号码写入 JSON 输出流
        jsonGenerator.writeString(mailDesensitizedUtil);
    }
}
