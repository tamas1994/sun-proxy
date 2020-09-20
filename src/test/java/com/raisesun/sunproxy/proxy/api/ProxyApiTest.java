package com.raisesun.sunproxy.proxy.api;

import com.google.gson.Gson;
import com.raisesun.sunproxy.proxy.bean.request.PickRequest;
import com.raisesun.sunproxy.proxy.bean.result.PickIpResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

@Slf4j
@SpringBootTest
public class ProxyApiTest {

    @Autowired
    ProxyApi proxyApi;

    @Test
    public void testPickIp() {

        PickRequest request = PickRequest.builder()
                .num(1)
                .type(2)
                .pack(42084)
                .port(1)
                .city(350500)
                .pro(350000)
                .ts(1)
                .ys(1)
                .cs(1)
                .lb(1)
                .pb(1)
                .regions("")
                .build();
        PickIpResult result = proxyApi.pickIp(request);
        log.info("result:{}", new Gson().toJson(result));
    }
}
