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


}
