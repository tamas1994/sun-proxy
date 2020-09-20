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

@Slf4j
@Service
public class ProxyService {

    @Autowired
    ProxyApi proxyApi;

    @Value("${sys.proxy.maxProxyNum}")
    private int maxProxyNum;

    private Queue<ProxyInfo> cacheList;

    static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Result pickIps(PickRequest pickRequest) {

        ProxyInfo proxyInfo = null;
        while (proxyInfo == null) {
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
            if(!pickIpResult.getCode().equals(ResultCode.SUCCESS.code())) {
                return Result.failure(ResultCode.PROXY_API_ERROR.code(), pickIpResult.getMsg());
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
                        .build();
                this.cacheList.offer(proxyItem);
            }
            return Result.failure(ResultCode.CACHE_IS_NULL);
        }

        long currentTime = System.currentTimeMillis();
        if(proxyInfo.getExpireTime() < currentTime) {
            return Result.failure(ResultCode.PROXY_HAS_EXPIRED);
        }

        return Result.success(proxyInfo);
    }

    public void recycle(ProxyInfo proxyInfo) {
        this.cacheList.offer(proxyInfo);
    }

    public Queue<ProxyInfo> getAll() {
        Queue<ProxyInfo> proxyQueue = cacheList;
        return proxyQueue;
    }

    @PostConstruct
    private void init() {
        if(this.cacheList == null) {
            this.cacheList = new LinkedList<>();
        }
    }
}
