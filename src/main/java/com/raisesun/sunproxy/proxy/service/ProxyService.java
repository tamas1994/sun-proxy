package com.raisesun.sunproxy.proxy.service;

import com.google.gson.Gson;
import com.raisesun.sunproxy.common.bean.Result;
import com.raisesun.sunproxy.common.bean.ResultCode;
import com.raisesun.sunproxy.proxy.api.ProxyApi;
import com.raisesun.sunproxy.proxy.bean.request.PickRequest;
import com.raisesun.sunproxy.proxy.bean.result.PickIpResult;
import com.raisesun.sunproxy.proxy.enity.ProxyInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
public class ProxyService {

    @Autowired
    ProxyApi proxyApi;

    @Value("${sys.proxy.maxProxyNum}")
    private int maxProxyNum;

    private BlockingQueue<ProxyInfo> cacheList;

    static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Result pickIps(PickRequest pickRequest) {

        int errTimes = 0;
        ProxyInfo proxyInfo = null;
        while (proxyInfo == null) {
            errTimes ++;
            if(errTimes > 50) {
                return Result.failure(-1, "重试超过50次");
            }
            Result<ProxyInfo> result = getProxy(pickRequest);
            if(result.getCode().equals(ResultCode.SUCCESS.code())) {
                proxyInfo = result.getData();
                log.info(new Gson().toJson(result));
                break;
            }

            if(result.getCode().equals(ResultCode.PROXY_API_ERROR.code())) {
                log.info(new Gson().toJson(result));
                return result;
            }

            if(result.getCode().intValue() == 113) {
                return Result.failure(result.getCode(), result.getMessage());
            }

            if(result.getCode().equals(ResultCode.CACHE_IS_NULL.code())) {
                // 加上代理池数量上限，避免过多消耗代理
                if(cacheList.size() > maxProxyNum) {
                    log.info(new Gson().toJson(result));
                    break;
                }
                continue;
            }

            // 代理已过期，需要重新获取代理
            if(result.getCode().equals(ResultCode.PROXY_HAS_EXPIRED.code())) {
                log.info(new Gson().toJson(result));
                continue;
            }
        }

        return Result.success(proxyInfo);
    }

    public Result<ProxyInfo> getProxy(PickRequest pickRequest) {

        ProxyInfo proxyInfo = this.cacheList.poll();
        if(proxyInfo == null) {
            PickIpResult pickIpResult = proxyApi.pickIp(pickRequest);
            if(pickIpResult == null) {
                return Result.failure(ResultCode.PROXY_API_ERROR);
            }

            if(!pickIpResult.getCode().equals(ResultCode.SUCCESS.code())) {
                return Result.failure(pickIpResult.getCode(), pickIpResult.getMsg());
            }

            List<PickIpResult.Ip> proxyList = pickIpResult.getData();
            for (PickIpResult.Ip item : proxyList) {
                LocalDateTime date = LocalDateTime.parse(item.getExpireTime(), fmt);
                long expireTime = date.toInstant(ZoneOffset.of("+8")).toEpochMilli();
                ProxyInfo proxyItem = ProxyInfo.builder()
                        .ip(item.getIp())
                        .port(item.getPort())
                        .city(item.getCity())
                        .expireTime(expireTime)
                        .pullTime(System.currentTimeMillis())
                        .build();
                this.cacheList.offer(proxyItem);
            }
            return Result.failure(ResultCode.CACHE_IS_NULL);
        }

        return isExpired(proxyInfo) ? Result.failure(ResultCode.PROXY_HAS_EXPIRED) :  Result.success(proxyInfo);
    }

    public void recycle(ProxyInfo proxyInfo) {
        // 如果已经过期，则不再回收
        if(isExpired(proxyInfo)) {
            return;
        }
        this.cacheList.offer(proxyInfo);
    }

    public Queue<ProxyInfo> getAll() {
        BlockingQueue<ProxyInfo> proxyQueue = this.cacheList;
        return proxyQueue;
    }

    public boolean isExpired(ProxyInfo proxyInfo) {
        long pullTIme = proxyInfo.getPullTime();
        long expiredTime = proxyInfo.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if(expiredTime < currentTime) {
            return true;
        }

        if(currentTime - pullTIme > 1000 * 60 * 30) {
            return true;
        }

        return false;
    }

    @PostConstruct
    private void init() {
        if(this.cacheList == null) {
            this.cacheList = new LinkedBlockingQueue<ProxyInfo>() {
            };
        }
    }
}
