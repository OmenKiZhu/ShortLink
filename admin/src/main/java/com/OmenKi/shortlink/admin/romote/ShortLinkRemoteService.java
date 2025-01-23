package com.OmenKi.shortlink.admin.romote;

import cn.hutool.http.HttpUtil;
import com.OmenKi.shortlink.admin.common.convention.result.Result;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkCreateReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkPageReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkUpdateReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkCreateRespDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkPageRespDTO;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/7
 * @Description: 短链接中台远程调用服务
 */
public interface ShortLinkRemoteService {
    /**
     * 创建短链接
     * @param requestParam
     * @return
     */
    default Result<ShortLinkCreateRespDTO> createShortLink(ShortLinkCreateReqDTO requestParam){
        String resultBodyStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/create", JSON.toJSONString(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<Result<ShortLinkCreateRespDTO>>() {});
    }

    /**
     * 分页查询短链接
     * @param requestParam
     * @return
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam){
        //使用Hutool 的httpUtil
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String ResultPageJsonStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/page", requestMap);

        return JSON.parseObject(ResultPageJsonStr, new TypeReference<Result<IPage<ShortLinkPageRespDTO>>>() {});
    }

    /**
     * 查询分组短链接总量
     * @param requestParam
     * @return
     */
    default Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(List<String> requestParam){
        //使用Hutool 的httpUtil
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requestParam", requestParam);
        String ResultPageJsonStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/count", requestMap);

        return JSON.parseObject(ResultPageJsonStr, new TypeReference<Result<List<ShortLinkGroupCountQueryRespDTO>>>() {});
    }

    /**
     * 更新短连接信息
      * @param requestParam
     */
    default void updateShortLink(ShortLinkUpdateReqDTO requestParam){
      HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/update", JSON.toJSONString(requestParam));
    }

}
