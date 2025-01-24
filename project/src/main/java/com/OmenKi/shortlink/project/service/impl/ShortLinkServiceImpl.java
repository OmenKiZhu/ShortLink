package com.OmenKi.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.OmenKi.shortlink.project.common.convention.exception.ClientException;
import com.OmenKi.shortlink.project.common.convention.exception.ServiceException;
import com.OmenKi.shortlink.project.common.enums.ValiDateTypeEnum;
import com.OmenKi.shortlink.project.dao.entity.ShortLinkDO;
import com.OmenKi.shortlink.project.dao.entity.ShortLinkGotoDO;
import com.OmenKi.shortlink.project.dao.mapper.ShortLinkGotoMapper;
import com.OmenKi.shortlink.project.dao.mapper.ShortLinkMapper;
import com.OmenKi.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.OmenKi.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.OmenKi.shortlink.project.service.ShortLinkService;
import com.OmenKi.shortlink.project.toolkit.HashUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/30
 * @Description: 短链接接口实现层
 * @Version: 1.0
 * @Copyright: Copyright (c) 2024
 * @History:
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;
    private final ShortLinkMapper shortLinkMapper;
    private final ShortLinkGotoMapper shortLinkGotoMapper;
    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        String shortLinkSuffix = generateSuffix(requestParam);
        String fullShortLink = requestParam.getDomain() + "/" + shortLinkSuffix;
        ShortLinkDO shortLinkDO = BeanUtil.toBean(requestParam, ShortLinkDO.class);
        shortLinkDO.setFullShortUrl(fullShortLink);
        shortLinkDO.setShortUri(shortLinkSuffix);
        shortLinkDO.setEnableStatus(0);
        shortLinkDO.setDescriptionAlias(requestParam.getDescriptionAlias());

        ShortLinkGotoDO shortLinkGotoDO = ShortLinkGotoDO.builder()
                .gid(requestParam.getGid())
                .fullShortUrl(fullShortLink)
                .build();
        try {
            baseMapper.insert(shortLinkDO);
            shortLinkGotoMapper.insert(shortLinkGotoDO);
        }catch (DuplicateKeyException e) {
            //TODO 已经误判的短链接如何处理 ---> 查库
            LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, fullShortLink);
            ShortLinkDO hasShortLinkDO = baseMapper.selectOne(queryWrapper);
            if(hasShortLinkDO != null) {
                log.warn("短链接:{} 重复入库", fullShortLink);
                throw new ServiceException("短链接生成重复");
            }
        }

        // 误判短链接存在的后续操作
        log.info("短链接新加入布隆过滤器过滤中---");
        shortUriCreateCachePenetrationBloomFilter.add(fullShortLink);
        return ShortLinkCreateRespDTO.builder()
                .gid(shortLinkDO.getGid())
                .originUrl(shortLinkDO.getOriginUrl())
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .build();
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0)
                .orderByDesc(ShortLinkDO::getCreateTime);
        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each -> BeanUtil.toBean(each, ShortLinkPageRespDTO.class));
    }

    @Override
    public List<ShortLinkGroupCountQueryRespDTO> listShortLinkGroupCount(List<String> requestParam) {
        QueryWrapper<ShortLinkDO> shortLinkDOQueryWrapper = Wrappers.query(new ShortLinkDO())
                .select("gid as gid, count(*) as shortLinkCount")
                .in("gid", requestParam)
                .eq("enable_status", 0)
                .groupBy("gid");
        List<Map<String, Object>> ShortLinkDOList = baseMapper.selectMaps(shortLinkDOQueryWrapper);

        return BeanUtil.copyToList(ShortLinkDOList, ShortLinkGroupCountQueryRespDTO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateShortLink(ShortLinkUpdateReqDTO requestParam) {
        //做一个查询
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0);

        ShortLinkDO hasShortLink = baseMapper.selectOne(queryWrapper);
        if(hasShortLink == null) {
            throw new ClientException("短连接记录不存在");
        }
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .gid(requestParam.getGid())
                .originUrl(requestParam.getOriginUrl())
                .descriptionAlias(requestParam.getDescriptionAlias())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .domain(hasShortLink.getDomain())
                .shortUri(hasShortLink.getShortUri())
                .clickNum(hasShortLink.getClickNum())
                .favicon(hasShortLink.getFavicon())
                .createType(hasShortLink.getCreateType())
                .build();
        if (Objects.equals(requestParam.getGid(), requestParam.getGid()))
        {
            LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLinkDO::getGid, requestParam.getGid())
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0)
                    .set(Objects.equals(requestParam.getValidDateType(), ValiDateTypeEnum.PERMANENT.getType()), ShortLinkDO::getValidDate, null);


            baseMapper.update(shortLinkDO, updateWrapper);
        }else {
            LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLinkDO::getGid, hasShortLink.getGid())
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0);


            int delete = baseMapper.delete(updateWrapper);
            shortLinkDO.setGid(requestParam.getGid());
            baseMapper.insert(shortLinkDO);
        }





    }

    @Override
    public void restoreUrl(String shortUri, ServletRequest request, ServletResponse response) throws IOException {
        String serverName = request.getServerName();
        String fullShortUrl = serverName + "/" + shortUri;
        LambdaQueryWrapper<ShortLinkGotoDO> gotoDOLambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
        ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(gotoDOLambdaQueryWrapper);
        if (shortLinkGotoDO == null) {
            //TODO 此处要进行封控
            return;
        }
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, shortLinkGotoDO.getGid())
                .eq(ShortLinkDO::getFullShortUrl, fullShortUrl)
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0);
        ShortLinkDO shortLinkDO = baseMapper.selectOne(queryWrapper);
        if(shortLinkDO != null) {
            ((HttpServletResponse) response).sendRedirect(shortLinkDO.getFullShortUrl());
        }

        if(!(shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl))) {

        }
    }


    private String generateSuffix(ShortLinkCreateReqDTO requestParam) {
        int customGenerateCount = 0;
        String shortUri;
        while(true) {
            if(customGenerateCount > 10) {
                log.info("布隆过滤器过滤判断中---");
                throw new ServiceException("经过布隆过滤器---短链接频繁生成，请稍后再试");
            }
            String originUrl = requestParam.getOriginUrl();
            originUrl += System.currentTimeMillis(); //后面加上毫秒数 将布隆冲突的概率降到最低
            shortUri= HashUtil.hashToBase62(originUrl);

            if(!shortUriCreateCachePenetrationBloomFilter.contains(requestParam.getDomain() + "/" + shortUri))
            {
                break;
            }
            else{
                customGenerateCount++;
            }
        }

        return shortUri;
    }
}
