package com.raisesun.sunproxy.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeUtil {

    /**
     * 延时0.5s到1.5s
     */
    public static void delayerRandomMPlusN(long baseMs, long randomMs) {

        long delayMs = (long)Math.ceil(Math.random()*randomMs)+baseMs;
        System.out.println(delayMs);
        try {
            Thread.currentThread().sleep(delayMs);
        } catch (InterruptedException e) {
            log.error("延时器调用异常，原因：{}", e.getMessage());
        }
    }

    public static void delayNMinutes(long ms) {
        if(ms > 0) {
            try {
                Thread.sleep(ms);
            } catch (Exception e) {
                log.error("延时器调用异常，原因：{}", e.getMessage());
            }
        }
    }

    /**
     * 延时10分钟
     */
    public static void delayTenMinutes() {
        long delayMilliSeconds = 1000*60*60*5;
        try {
            Thread.currentThread().sleep(delayMilliSeconds);
        } catch (InterruptedException e) {
            log.error("延时器调用异常，原因：{}", e.getMessage());
        }
    }
}
