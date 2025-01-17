package com.OmenKi.shortlink.admin.common.serialize;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/11/28
 * @Description: 手机号脱敏反序列化
 */
public class PhoneDesensitizationSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String phone, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String mobilePhoneDesensitizedUtil = DesensitizedUtil.mobilePhone(phone);
        // 将脱敏后的电话号码写入 JSON 输出流
        jsonGenerator.writeString(mobilePhoneDesensitizedUtil);
    }
}
