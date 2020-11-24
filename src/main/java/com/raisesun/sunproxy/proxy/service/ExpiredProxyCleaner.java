package com.raisesun.sunproxy.proxy.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 过期代理定时清除处理类
 */
@Component
public class ExpiredProxyCleaner {

    @Scheduled(fixedDelay = 1000 * 10)
    public void clean() {


    }
}
