package com.raisesun.sunproxy.proxy.enity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class ProxyInfo {

    private String ip;
    private String port;
    private String city;
    private long expireTime;
    private long pullTime;

    @Tolerate
    public ProxyInfo() {
    }
}
