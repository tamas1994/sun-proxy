package com.raisesun.sunproxy.proxy.bean.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PickIpResult {

    private Integer code;
    private List<Ip> data;
    private String msg;

    @Data
    public class Ip {

        private String ip;
        private String port;
        private String city;
        @SerializedName("expire_time")
        private String expireTime;
    }
}
