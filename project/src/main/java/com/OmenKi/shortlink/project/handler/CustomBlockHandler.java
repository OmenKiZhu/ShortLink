package com.OmenKi.shortlink.project.handler;

import com.OmenKi.shortlink.project.common.convention.result.Result;
import com.OmenKi.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/14
 * @Description: 自定义流量测控的处理策略
 */
public class CustomBlockHandler {
    public static Result<ShortLinkCreateRespDTO> createShortLinkBlockHandlerMethod(ShortLinkCreateReqDTO requestParam, BlockException exception) {
        return new Result<ShortLinkCreateRespDTO>().setCode("B100000").setMessage("当前访问网站人数过多，请稍后再试...");
    }
}
