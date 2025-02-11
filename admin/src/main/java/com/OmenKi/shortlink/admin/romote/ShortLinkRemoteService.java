package com.OmenKi.shortlink.admin.romote;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.OmenKi.shortlink.admin.common.convention.result.Result;
import com.OmenKi.shortlink.admin.dto.req.RecycleBinRecoverReqDTO;
import com.OmenKi.shortlink.admin.dto.req.RecycleBinRemoveReqDTO;
import com.OmenKi.shortlink.admin.dto.req.RecycleBinSaveReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkStatsReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkStatsRespDTO;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkCreateReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkPageReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.req.ShortLinkUpdateReqDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkCreateRespDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.OmenKi.shortlink.admin.romote.dto.resp.ShortLinkPageRespDTO;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * 根据URL
      * @param url
     * @return
     */
    default Result<String> getTitleByUrl(@RequestParam("url") String url){
        String resultJson = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/title?url=" + url);
        //String jsonStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/title?url", url);
        return JSON.parseObject(resultJson, new TypeReference<>() {
        });
    }

    /**
     * 保存回收站
     * @param requestParam
     * @return
     */
    default void saveRecycleBin(RecycleBinSaveReqDTO requestParam){
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/save", JSON.toJSONString(requestParam));
    }



    /**
     * 分页查询回收站
     * @param requestParam
     * @return
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam){
        //使用Hutool 的httpUtil
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gidList", requestParam.getGidList());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String ResultPageJsonStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/page", requestMap);

        return JSON.parseObject(ResultPageJsonStr, new TypeReference<Result<IPage<ShortLinkPageRespDTO>>>() {});
    }

    /**
     * 回收站恢复短链接
     * @param requestParam
     */
    default void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam) {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/recover", JSON.toJSONString(requestParam));
    }

    /**
     * 回收站删除短连接功能
     * @param requestParam
     */
    default void removeRecycleBin(RecycleBinRemoveReqDTO requestParam) {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/remove", JSON.toJSONString(requestParam));
    }

    /**
     * 访问单个短链接指定时间内监控数据
     *
     * @param requestParam 访问短链接监控请求参数
     * @return 短链接监控信息
     */
    default Result<ShortLinkStatsRespDTO> oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/stats", BeanUtil.beanToMap(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {
        });
    }
}

