package com.raisesun.sunproxy.proxy.web;

import com.raisesun.sunproxy.common.bean.Result;
import com.raisesun.sunproxy.common.util.TimeUtil;
import com.raisesun.sunproxy.proxy.bean.request.PickRequest;
import com.raisesun.sunproxy.proxy.enity.ProxyInfo;
import com.raisesun.sunproxy.proxy.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

@RestController
@RequestMapping("proxy")
public class ProxyController {

    @Autowired
    ProxyService proxyService;

    @PostMapping("get")
    public Result get(@RequestParam(value = "num", defaultValue = "10") String num,
                      @RequestParam(value = "type", defaultValue = "2") String type,
                      @RequestParam(value = "pack", defaultValue = "42084") String pack,
                      @RequestParam(value = "port", defaultValue = "1") String port,
                      @RequestParam(value = "pro", defaultValue = "") String pro,
                      @RequestParam(value = "city", defaultValue = "") String city,
                      @RequestParam(value = "ts", defaultValue = "1") String ts,
                      @RequestParam(value = "ys", defaultValue = "1") String ys,
                      @RequestParam(value = "cs", defaultValue = "1") String cs,
                      @RequestParam(value = "lb", defaultValue = "1") String lb,
                      @RequestParam(value = "pb", defaultValue = "4") String pb,
                      @RequestParam(value = "regions", defaultValue = "") String regions) {

        Queue<ProxyInfo> cacheProxy = proxyService.getAll();
        if(CollectionUtils.isEmpty(cacheProxy)) {
            // 如果代理池缓存为空，则延时1到3秒再请求
            TimeUtil.delayerRandomMPlusN(1000l, 2000l);
        }

        PickRequest request = PickRequest.builder()
                .num(num).type(type).pack(pack).port(port).pro(pro).city(city)
                .ts(ts).ys(ys).cs(cs).lb(lb).pb(pb).regions(regions).build();
        Result result = proxyService.pickIps(request);
        return result;
    }

    @PostMapping("recycle")
    public Result recycle(ProxyInfo proxyInfo) {
        if(proxyInfo != null) {
            proxyService.recycle(proxyInfo);
        }
        return Result.success();
    }

    @GetMapping("getAll")
    public Result<Queue<ProxyInfo>> getAll() {
        Queue<ProxyInfo> proxyQueue = proxyService.getAll();
        return Result.success(proxyQueue);
    }
}
