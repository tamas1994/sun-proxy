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
    private String num;


    private String type;

    /**
     * 套餐包
     */
    private String pack;

    /**
     * 端口赖兴
     */
    private String port;

    /**
     * 代理城市
     */
    private String city;

    /**
     * 代理省份
     */
    private String pro;

    private String ts;
    private String ys;
    private String cs;
    private String lb;
    private String pb;
    private String regions;

    @Tolerate
    public PickRequest() {
    }
}
