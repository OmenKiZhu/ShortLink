package com.OmenKi.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Week;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.OmenKi.shortlink.project.common.constants.ShortLinkConstant;
import com.OmenKi.shortlink.project.common.convention.exception.ClientException;
import com.OmenKi.shortlink.project.common.convention.exception.ServiceException;
import com.OmenKi.shortlink.project.common.enums.ValiDateTypeEnum;
import com.OmenKi.shortlink.project.dao.entity.*;
import com.OmenKi.shortlink.project.dao.mapper.*;
import com.OmenKi.shortlink.project.dto.req.ShortLinkBatchCreateReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.OmenKi.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.OmenKi.shortlink.project.dto.resp.*;
import com.OmenKi.shortlink.project.service.ShortLinkService;
import com.OmenKi.shortlink.project.toolkit.HashUtil;
import com.OmenKi.shortlink.project.toolkit.LinkUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.OmenKi.shortlink.project.common.constants.RedisKeyConstant.*;

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
    private final ShortLinkGotoMapper shortLinkGotoMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;
    private final LinkStatsTodayMapper linkStatsTodayMapper;

    @Value("${short-link.stats.locale.amap-key}")
    private String statsLocaleAmapKey;

    @Value("${short-link.domain.default}")
    private String createShortLinkDefaultDomain;
    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        //产生链接的后缀
        String shortLinkSuffix = generateSuffix(requestParam);

        String fullShortLink = createShortLinkDefaultDomain + "/" + shortLinkSuffix;

        ShortLinkDO shortLinkDO = BeanUtil.toBean(requestParam, ShortLinkDO.class);
        shortLinkDO.setFullShortUrl(fullShortLink);
        shortLinkDO.setShortUri(shortLinkSuffix);
        shortLinkDO.setDomain(createShortLinkDefaultDomain);
        shortLinkDO.setEnableStatus(0);
        shortLinkDO.setDescriptionAlias(requestParam.getDescriptionAlias());
        shortLinkDO.setFavicon(getFavicon(requestParam.getOriginUrl()));
        shortLinkDO.setTotalPv(0);
        shortLinkDO.setTotalUv(0);
        shortLinkDO.setTotalUip(0);

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
        //短连接缓存预热
        log.info("短链接:{} 预热中---", fullShortLink);

        //默认一个月过期
        stringRedisTemplate.opsForValue().set(String.format(GOTO_SHORT_LINK_KEY, fullShortLink),
                requestParam.getOriginUrl(),
                LinkUtil.getShortLinkCacheValidTime(requestParam.getValidDate()),
                TimeUnit.MILLISECONDS);
        log.info("短链接:{} 预热完成---", fullShortLink);

        // 误判短链接存在的后续操作
        log.info("短链接新加入布隆过滤器过滤中---");
        shortUriCreateCachePenetrationBloomFilter.add(fullShortLink);
        return ShortLinkCreateRespDTO.builder()
                .gid(shortLinkDO.getGid())
                .originUrl(shortLinkDO.getOriginUrl())
                .fullShortUrl("http://" + shortLinkDO.getFullShortUrl())
                .build();
    }

    /**
     * 分页查询短链接
     * @param requestParam
     * @return
     */
    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
//        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
//                .eq(ShortLinkDO::getGid, requestParam.getGid())
//                .eq(ShortLinkDO::getDelFlag, 0)
//                .eq(ShortLinkDO::getEnableStatus, 0)
//                .orderByDesc(ShortLinkDO::getCreateTime);
//        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
//        return resultPage.convert(each -> {
//            ShortLinkPageRespDTO result = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);

            //替换成pageLink的调用
            IPage<ShortLinkDO> resultPage = baseMapper.pageLink(requestParam);
            return resultPage.convert(each -> {
                ShortLinkPageRespDTO result = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);
                result.setDomain("http://" + result.getDomain());
                return result;
            });
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
        //获取请求链接的域名
        String serverName = request.getServerName();

        //拿到请求的端口号(如果请求为80 则设置为“”， 后续跳转到404)
        String serverPort = Optional.of(request.getServerPort())
                .filter(each -> !Objects.equals(each, 80))
                .map(String::valueOf)
                .map(each -> ":" + each)
                .orElse("");

        //进行fullShortUrl的拼接
        String fullShortUrl = serverName + serverPort + "/" + shortUri;

        String originalLink = stringRedisTemplate.opsForValue().get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(originalLink)) {
            log.info("从缓存中获取跳转的原始链接----");
            shortLinkStats(fullShortUrl, null, request, response);
            ((HttpServletResponse) response).sendRedirect(originalLink);
            return;
        }
        //布隆过滤器判断是否为空（布隆过滤器在短链接创建的到时候就加入进去了）
        boolean isContained = shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl);
        if(!isContained) {
            ((HttpServletResponse) response).sendRedirect("/page/notfound");
            return;
        }
        String gotoIsNullShortLink = stringRedisTemplate.opsForValue().get(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl));
        if(StrUtil.isNotBlank(gotoIsNullShortLink)) {
            log.info("从redis中获取到该短链接对应的原始链接----");
            ((HttpServletResponse) response).sendRedirect("/page/notfound");
            return;
        }

        // n+ 请求准备访问数据库
        //解决缓存击穿 redis key 失效或者不存在后 大量请求涌入数据库 ---》 引入分布式锁 只让一个请求访问db

        //获取到这把锁
        RLock lock = redissonClient.getLock(String.format(LOCK_GOTO_SHORT_LINK_KEY, fullShortUrl));
        lock.lock();
        try {
            //双重判定 可能前面一次已经设置好缓存 后续的操作不用再从数据库获取 因此再判定一次
            originalLink = stringRedisTemplate.opsForValue().get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl));
            if(StrUtil.isNotBlank(originalLink)) {
                log.info("之后的短链接请求已经从第一次的请求存入到缓存获取到原始链接----");
                ((HttpServletResponse) response).sendRedirect(originalLink);
                return;
            }
            LambdaQueryWrapper<ShortLinkGotoDO> gotoDOLambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                    .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
            ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(gotoDOLambdaQueryWrapper);
            if (shortLinkGotoDO == null) {
                //设置个缓存空值，避免缓存穿透
                stringRedisTemplate.opsForValue().set(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl), "-", 30, TimeUnit.MINUTES);
                ((HttpServletResponse) response).sendRedirect("/page/notfound");
                return;
            }
            LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getGid, shortLinkGotoDO.getGid())
                    .eq(ShortLinkDO::getFullShortUrl, fullShortUrl)
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0);
            ShortLinkDO shortLinkDO = baseMapper.selectOne(queryWrapper);
            if(shortLinkDO == null || (shortLinkDO.getValidDate() != null && shortLinkDO.getValidDate().before(new Date()))) {
                //当作没短链接处理 设置个缓存空值，避免缓存穿透
                stringRedisTemplate.opsForValue().set(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl), "-", 30, TimeUnit.MINUTES);
                ((HttpServletResponse) response).sendRedirect("/page/notfound");
                return;
            }
            stringRedisTemplate.opsForValue().set(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl),
                    shortLinkDO.getOriginUrl(),
                    LinkUtil.getShortLinkCacheValidTime(shortLinkDO.getValidDate()),
                    TimeUnit.MILLISECONDS);
            log.info("第一次请求将原始链接存入缓存----");
            shortLinkStats(fullShortUrl, shortLinkDO.getGid(), request, response);
            ((HttpServletResponse) response).sendRedirect(shortLinkDO.getOriginUrl());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ShortLinkBatchCreateRespDTO  batchCreateShortLink(ShortLinkBatchCreateReqDTO requestParam) {
        List<String> originUrls = requestParam.getOriginUrls();
        List<String> describes = requestParam.getDescribes();
        List<ShortLinkBaseInfoRespDTO> result = new ArrayList<>();
        for (int i = 0; i < originUrls.size(); i++) {
            ShortLinkCreateReqDTO shortLinkCreateReqDTO = BeanUtil.toBean(requestParam, ShortLinkCreateReqDTO.class);
            shortLinkCreateReqDTO.setOriginUrl(originUrls.get(i));
            shortLinkCreateReqDTO.setDescriptionAlias(describes.get(i));
            try {
                ShortLinkCreateRespDTO shortLink = createShortLink(shortLinkCreateReqDTO);
                ShortLinkBaseInfoRespDTO linkBaseInfoRespDTO = ShortLinkBaseInfoRespDTO.builder()
                        .fullShortUrl(shortLink.getFullShortUrl())
                        .originUrl(shortLink.getOriginUrl())
                        .describe(describes.get(i))
                        .build();
                result.add(linkBaseInfoRespDTO);
            } catch (Throwable ex) {
                log.error("批量创建短链接失败，原始参数：{}", originUrls.get(i));
            }
        }
        return ShortLinkBatchCreateRespDTO.builder()
                .total(result.size())
                .baseLinkInfos(result)
                .build();
    }

    private void shortLinkStats(String fullShortUrl, String gid, ServletRequest request, ServletResponse response) {
        AtomicBoolean uvFirstFlag = new AtomicBoolean();
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();

       try {
           AtomicReference<String> uv = new AtomicReference<>();
           Runnable addResponseCookieTask = () -> {
               //request判断不携带uvCookie
               //响应体设置cookie
               String actualUv = UUID.fastUUID().toString();
               uv.set(actualUv);
               Cookie uvCookie = new Cookie("uv", uv.get());
               uvCookie.setMaxAge(60 * 60 * 24 * 30); //过期时间为30天
               uvCookie.setPath(StrUtil.sub(fullShortUrl, fullShortUrl.indexOf("/"), fullShortUrl.length()));
               ((HttpServletResponse) response).addCookie(uvCookie);
               uvFirstFlag.set(Boolean.TRUE);
               Long added = stringRedisTemplate.opsForSet().add("short-link:stats:uv:" + fullShortUrl, uv.get());
           };

           //判断request是否携带cookie
           if(ArrayUtil.isNotEmpty(cookies)) {
               //判断是否包含我们需要的uvCookie
               Arrays.stream(cookies)
                       .filter(each -> Objects.equals(each.getName(), "uv"))
                       .findFirst()
                       .map(Cookie::getValue)
                       .ifPresentOrElse(each -> {
                           uv.set(each);
                           Long added = stringRedisTemplate.opsForSet().add("short-link:stats:uv:" + fullShortUrl, each);
                           //利用这个flag标识来判断最后是否需要UV+1
                           uvFirstFlag.set(added != null && added > 0L);

                       }, addResponseCookieTask);
           }else {
               addResponseCookieTask.run();
           }

           //判断uip是否一直的flag
           String remoteAddr = request.getRemoteAddr();
           Long uipAdded = stringRedisTemplate.opsForSet().add("short-link:stats:uip:" + fullShortUrl, remoteAddr);
           Boolean uipFirstFlag = uipAdded != null && uipAdded > 0L;

           if (StrUtil.isBlank(gid)) {
               LambdaQueryWrapper<ShortLinkGotoDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                       .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
               ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(queryWrapper);
               gid = shortLinkGotoDO.getGid();
           }
           int hour = DateUtil.hour(new Date(), true); //获取当前小时
           Week week = DateUtil.dayOfWeekEnum(new Date());  //获取指定日期是星期几
           int weekValue = week.getValue();

           LinkAccessStatsDO linkAccessStatsDO = LinkAccessStatsDO.builder()
                   .pv(1)
                   .uv(uvFirstFlag.get() ? 1 : 0)
                   .uip(uipFirstFlag ? 1 : 0)
                   .hour(hour)
                   .weekday(weekValue)
                   .fullShortUrl(fullShortUrl)
                   .gid(gid)
                   .date(new Date())
                   .build();

           linkAccessStatsMapper.shortLinkStats(linkAccessStatsDO);

           Map<String, Object> localeParamMap = new HashMap<>();
           localeParamMap.put("key", statsLocaleAmapKey);
           localeParamMap.put("ip", remoteAddr);
           String localeResultStr = HttpUtil.get(ShortLinkConstant.AMAP_REMOTE_URL, localeParamMap);
           JSONObject localeResultObj = JSON.parseObject(localeResultStr);
           String infoCode = localeResultObj.getString("infocode");
           String actualProvince;
           String actualCity;
           if(StrUtil.isNotBlank(infoCode) && Objects.equals(infoCode, "10000")) {
               String province = localeResultObj.getString("province");
               Boolean unKnowFlag = StrUtil.equals(province, "[]");
               LinkLocaleStatsDO linkLocaleStatsDO = LinkLocaleStatsDO.builder()
                       .fullShortUrl(fullShortUrl)
                       .province(actualProvince = unKnowFlag ? "未知" : localeResultObj.getString("province"))
                       .city(actualCity = unKnowFlag ? "未知" : localeResultObj.getString("city"))
                       .adcode(unKnowFlag ? "未知" : localeResultObj.getString("adcode"))
                       .cnt(1)
                       .country("中国")
                       .gid(gid)
                       .date(new Date())
                       .build();
               linkLocaleStatsMapper.shortLinkScaleStats(linkLocaleStatsDO);

               String os = LinkUtil.getOs((HttpServletRequest) request);
               LinkOsStatsDO linkOsStatsDO = LinkOsStatsDO.builder()
                       .os(os)
                       .fullShortUrl(fullShortUrl)
                       .cnt(1)
                       .gid(gid)
                       .date(new Date())
                       .build();
               linkOsStatsMapper.shortLinkOsStats(linkOsStatsDO);
               String browser = LinkUtil.getBrowser(((HttpServletRequest) request));
               LinkBrowserStatsDO linkBrowserStatsDO = LinkBrowserStatsDO.builder()
                       .browser(browser)
                       .cnt(1)
                       .gid(gid)
                       .fullShortUrl(fullShortUrl)
                       .date(new Date())
                       .build();
               linkBrowserStatsMapper.shortLinkBrowserState(linkBrowserStatsDO);

               String device = LinkUtil.getDevice((HttpServletRequest) request);
               LinkDeviceStatsDO linkDeviceStatsDO = LinkDeviceStatsDO.builder()
                       .device(device)
                       .fullShortUrl(fullShortUrl)
                       .cnt(1)
                       .gid(gid)
                       .date(new Date())
                       .build();
               linkDeviceStatsMapper.shortLinkDeviceState(linkDeviceStatsDO);

               String network = LinkUtil.getNetwork(((HttpServletRequest) request));
               LinkNetworkStatsDO linkNetworkStatsDO = LinkNetworkStatsDO.builder()
                       .network(network)
                       .cnt(1)
                       .gid(gid)
                       .fullShortUrl(fullShortUrl)
                       .date(new Date())
                       .build();
               linkNetworkStatsMapper.shortLinkNetworkState(linkNetworkStatsDO);

               LinkAccessLogsDO linkAccessLogsDO = LinkAccessLogsDO.builder()
                       .ip(remoteAddr)
                       .browser(browser)
                       .device(device)
                       .network(network)
                       .locale(StrUtil.join("-", "中国", actualProvince, actualCity))
                       .os(os)
                       .gid(gid)
                       .fullShortUrl(fullShortUrl)
                       .user(uv.get())
                       .build();
               linkAccessLogsMapper.insert(linkAccessLogsDO);

               baseMapper.incrementStats(gid, fullShortUrl, 1, uvFirstFlag.get() ? 1 : 0, uipFirstFlag ? 1 : 0);

               LinkStatsTodayDO linkStatsTodayDO = LinkStatsTodayDO.builder()
                       .gid(gid)
                       .fullShortUrl(fullShortUrl)
                       .date(new Date())
                       .todayPv(1)
                       .todayUv(uvFirstFlag.get() ? 1 : 0)
                       .todayUip(uipFirstFlag ? 1 : 0)
                       .build();
               linkStatsTodayMapper.shortLinkTodayStats(linkStatsTodayDO);
           }
       } catch (Throwable ex) {
           log.info("短链接访问量统计异常", ex);
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

    @SneakyThrows
    private String getFavicon(String url) {
        URL targetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            String redirectUrl = connection.getHeaderField("Location");
            if (redirectUrl != null) {
                URL newUrl = new URL(redirectUrl);
                connection = (HttpURLConnection) newUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                responseCode = connection.getResponseCode();
            }
        }
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Document document = Jsoup.connect(url).get();
            Element faviconLink = document.select("link[rel~=(?i)^(shortcut )?icon]").first();
            if (faviconLink != null) {
                return faviconLink.attr("abs:href");
            }
        }
        return null;
    }
}
