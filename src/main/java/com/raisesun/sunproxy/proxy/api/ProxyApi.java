package com.raisesun.sunproxy.proxy.api;

import com.google.gson.Gson;
import com.raisesun.sunproxy.common.util.OkHttpUtil;
import com.raisesun.sunproxy.proxy.bean.request.PickRequest;
import com.raisesun.sunproxy.proxy.bean.result.PickIpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * 太阳代理接口
 */
@Slf4j
@Component
public class ProxyApi {

    @Value("http://http.tiqu.qingjuhe.cn/getip")
    private String pickIpUrl;

    public PickIpResult pickIp(PickRequest request) {

        try {
            Map<String, String> params = BeanUtils.describe(request);
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(pickIpUrl).append("?");
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                if (iterator.hasNext()) {
                    urlBuilder.append("&");
                }
            }
            String url = urlBuilder.toString();
            log.info("url:{}", url);
            String reStr = OkHttpUtil.doGet(url);
            PickIpResult result = new Gson().fromJson(reStr, PickIpResult.class);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
