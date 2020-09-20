package com.raisesun.sunproxy.common.bean;

public enum  ResultCode {
    /* 成功状态码 */
    SUCCESS(0, "成功"),
    PROXY_API_ERROR(10000, "调用太阳代理接口失败，请检查参数是否正确"),
    CACHE_IS_NULL(10001, "代理池数量为空，正在补充代理，请重新获取"),
    PROXY_HAS_EXPIRED(10002, "获取的代理已过期，请重新获取");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }



    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
