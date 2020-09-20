package com.raisesun.sunproxy.proxy.bean.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * create by misaka on 2020/09/19
 * 提取ip请求参数类
 */
@Data
@Builder
public class PickRequest {

    /**
     * 获取代理数量
     */
    private Integer num;


    private Integer type;

    /**
     * 套餐包
     */
    private Integer pack;

    /**
     * 端口赖兴
     */
    private Integer port;

    /**
     * 代理城市
     */
    private Integer city;

    /**
     * 代理省份
     */
    private Integer pro;

    private Integer ts;
    private Integer ys;
    private Integer cs;
    private Integer lb;
    private Integer pb;
    private String regions;

    @Tolerate
    public PickRequest() {
    }
}
