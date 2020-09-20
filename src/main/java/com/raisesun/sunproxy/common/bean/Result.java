package com.raisesun.sunproxy.common.bean;

import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;

    @Tolerate
    public <T>Result() {

    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(ResultCode resultCode, T data) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.data = data;
    }

    public Result(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public static <T>Result<T> success() {
        Result result = new Result(ResultCode.SUCCESS);
        return result;
    }

    public static <T>Result<T> success(T data) {
        Result<T> result = new <T>Result(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static <T>Result<T> success(String message) {
        Result result = new Result(ResultCode.SUCCESS);
        result.setMessage(message);
        return result;
    }

    public static <T>Result<T> result(ResultCode resultCode) {
        Result result = new Result(resultCode);
        return result;
    }

    public static <T>Result<T> failure(ResultCode resultCode) {
        Result result = new Result(resultCode);
        return result;
    }

    public static <T>Result<T> failure(ResultCode resultCode, T data) {
        Result result = new Result(resultCode);
        result.setData(data);
        return result;
    }

    public static <T>Result<T> failure(Integer code, String message) {
        Result result = new Result(code, message);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
