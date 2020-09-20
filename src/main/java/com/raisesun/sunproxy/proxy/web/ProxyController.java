package com.raisesun.sunproxy.proxy.web;

import com.raisesun.sunproxy.common.bean.Result;
import com.raisesun.sunproxy.proxy.bean.request.PickRequest;
import com.raisesun.sunproxy.proxy.enity.ProxyInfo;
import com.raisesun.sunproxy.proxy.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Queue;

@RestController
@RequestMapping("proxy")
public class ProxyController {

    @Autowired
    ProxyService proxyService;

    @PostMapping("get")
    public Result get(@RequestParam(value = "num", defaultValue = "10") int num,
                      @RequestParam(value = "type", defaultValue = "2") int type,
                      @RequestParam(value = "pack", defaultValue = "42084") int pack,
                      @RequestParam(value = "port", defaultValue = "1") int port,
                      @RequestParam(value = "pro", defaultValue = "350000") int pro,
                      @RequestParam(value = "city", defaultValue = "350600") int city,
                      @RequestParam(value = "ts", defaultValue = "1") int ts,
                      @RequestParam(value = "ys", defaultValue = "1") int ys,
                      @RequestParam(value = "cs", defaultValue = "1") int cs,
                      @RequestParam(value = "lb", defaultValue = "1") int lb,
                      @RequestParam(value = "pb", defaultValue = "4") int pb,
                      @RequestParam(value = "regions", defaultValue = "") String regions) {

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
    public Result getAll() {
        Queue<ProxyInfo> proxyQueue = proxyService.getAll();
        return Result.success(proxyQueue);
    }
}
