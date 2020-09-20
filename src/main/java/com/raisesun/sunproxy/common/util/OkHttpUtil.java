package com.raisesun.sunproxy.common.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.CollectionUtils;

import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Http请求工具类
 * Created by tamas on 2018/6/22.
 */
public class OkHttpUtil {

    private static final String DEFAULT_CHARSET = "UTF-8";
    public static final String POST_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String POST_TYPE_JSON = "application/json";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .build();

    /**
     * 获取get方法response
     * @param url
     * @return
     * @throws Exception
     */
    public static Response getResponse(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        return response;
    }

    /**
     * 获取get方法response
     * @param url
     * @return
     * @throws Exception
     */
    public static Response getResponse(String url, Proxy proxy) throws Exception {
        OkHttpClient proxyClient = new OkHttpClient().newBuilder().proxy(proxy).build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = proxyClient.newCall(request).execute();
        return response;
    }

    /**
     * 获取get方法response
     * @param url
     * @return
     * @throws Exception
     */
    public static Response getResponse(String url, Map<String, String> headers) throws Exception {
        OkHttpClient proxyClient = new OkHttpClient.Builder().followRedirects(false).build();
        Request request = new Request.Builder()
                .headers(parseHeaders(headers))
                .url(url)
                .get()
                .build();
        Response response = proxyClient.newCall(request).execute();
        return response;
    }

    /**
     * 获取get方法response
     * @param url
     * @return
     * @throws Exception
     */
    public static Response getResponse(String url, Map<String, String> headers, Proxy proxy) throws Exception {
         OkHttpClient proxyClient = new OkHttpClient.Builder().followRedirects(false).proxy(proxy).build();
        Request request = new Request.Builder()
                .headers(parseHeaders(headers))
                .url(url)
                .get()
                .build();
        Response response = proxyClient.newCall(request).execute();
        return response;
    }

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static String doGet(String url) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static String doGet(String url, Proxy proxy) throws Exception {

        OkHttpClient proxyClient = client.newBuilder().proxy(proxy).build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = proxyClient.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @return
     */
    public static String doGet(String url, Map<String, String> headers, Proxy proxy) throws Exception {

        OkHttpClient proxyClient = client.newBuilder().proxy(proxy).build();
        Request request = new Request.Builder()
                .headers(parseHeaders(headers))
                .url(url)
                .get()
                .build();
        Response response = proxyClient.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @return
     */
    public static String doGet(String url, Map<String, String> headers) throws Exception {

        Request request = new Request.Builder()
                .headers(parseHeaders(headers))
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Response postResponse(String url, String params, Map<String, String> headers) throws Exception {

        RequestBody requestBody = RequestBody.create(MediaType.parse(POST_TYPE_JSON), params);
        Request request = new Request.Builder()
                .headers(parseHeaders(headers))
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String doPost(String url, String params, Map<String, String> headers) throws Exception {

        RequestBody requestBody = RequestBody.create(MediaType.parse(POST_TYPE_JSON), params);
        Request request = new Request.Builder()
                .headers(parseHeaders(headers))
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, String params, Proxy proxy) throws Exception {

        OkHttpClient proxyClient = client.newBuilder().proxy(proxy).build();
        RequestBody requestBody = RequestBody.create(MediaType.parse(POST_TYPE_JSON), params);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = proxyClient.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String doPost(String url, String params, Map<String, String> headers, Proxy proxy) throws Exception {
        OkHttpClient proxyClient = client.newBuilder().proxy(proxy).build();
        RequestBody requestBody = RequestBody.create(MediaType.parse(POST_TYPE_JSON), params);
        Request request = new Request.Builder()
                .headers(parseHeaders(headers))
                .url(url)
                .post(requestBody)
                .build();
        Response response = proxyClient.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    public static String doPost(String url, String params, String type, Map<String, String> headers) throws Exception {
        RequestBody requestBody = FormBody.create(MediaType.parse(type), params);
        Request request = new Request.Builder()
                .url(url)
                .headers(parseHeaders(headers))
                .post(requestBody)
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    public static String doPost(String url, String params, String type, Map<String, String> headers, Proxy proxy) throws Exception {
        OkHttpClient proxyClient = client.newBuilder().proxy(proxy).build();
        RequestBody requestBody = FormBody.create(MediaType.parse(type), params);
        Request request = new Request.Builder()
                .url(url)
                .headers(parseHeaders(headers))
                .post(requestBody)
                .build();
        Response response = proxyClient.newCall(request).execute();
        byte[] bytes = response.body().bytes();
        String result = new String(bytes, StandardCharsets.UTF_8);
        response.close();
        return result;
    }

    public static String doPost(String url, String params, String type, Proxy proxy) throws Exception {
        OkHttpClient proxyClient = client.newBuilder().proxy(proxy).build();
        RequestBody requestBody = FormBody.create(MediaType.parse(type), params);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = proxyClient.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    public static String doPost(String url, String params, String type) throws Exception {

        RequestBody requestBody = RequestBody.create(MediaType.parse(type), params);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    /**
     * post请求 表单提交
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map<String, String> params) throws Exception {
        StringBuilder paramsBuidler = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            paramsBuidler.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                paramsBuidler.append("&");
            }
        }

        RequestBody requestBody = FormBody.create(MediaType.parse(POST_TYPE_FORM), paramsBuidler.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    /**
     * post请求 表单提交
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map<String, String> params, Proxy proxy) throws Exception {
        OkHttpClient proxyClient = client.newBuilder().proxy(proxy).build();
        StringBuilder paramsBuidler = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            paramsBuidler.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                paramsBuidler.append("&");
            }
        }
        RequestBody requestBody = FormBody.create(MediaType.parse(POST_TYPE_FORM), paramsBuidler.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = proxyClient.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    /**
     * post请求 表单提交
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String doPost(String url, Map<String, String> params, Map<String, String> headers, Proxy proxy) throws Exception {
        OkHttpClient proxyClient = client.newBuilder().proxy(proxy).build();
        StringBuilder paramsBuidler = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            paramsBuidler.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                paramsBuidler.append("&");
            }
        }
        RequestBody requestBody = FormBody.create(MediaType.parse(POST_TYPE_FORM), paramsBuidler.toString());
        Request request = new Request.Builder()
                .headers(parseHeaders(headers))
                .url(url)
                .post(requestBody)
                .build();
        Response response = proxyClient.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    /**
     * post请求 表单提交
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String doPost(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        StringBuilder paramsBuidler = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            paramsBuidler.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                paramsBuidler.append("&");
            }
        }
        RequestBody requestBody = FormBody.create(MediaType.parse(POST_TYPE_FORM), paramsBuidler.toString());
        Request request = new Request.Builder()
                .headers(parseHeaders(headers))
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        response.close();
        return result;
    }

    public static String getCookie(Response response) {

        StringBuilder cookieBuilder = new StringBuilder();
        List<String> cookies = response.headers("Set-Cookie");
        if(!CollectionUtils.isEmpty(cookies)) {
            Iterator<String> it = cookies.iterator();
            while(it.hasNext()) {
                String item = it.next().split(";")[0];
                cookieBuilder.append(item);
                if(it.hasNext()) {
                    cookieBuilder.append("; ");
                }
            }
        }
        return cookieBuilder.toString();
    }

    public static String getCookie(Response response, String field) {

        if(!response.isSuccessful()) {
            return "";
        }

        StringBuilder cookieBuilder = new StringBuilder();
        List<String> cookies = response.headers("Set-Cookie");
        if(!CollectionUtils.isEmpty(cookies)) {
            Iterator<String> it = cookies.iterator();
            while(it.hasNext()) {
                String item = it.next().split(";")[0];
                cookieBuilder.append(item);
                if(it.hasNext()) {
                    cookieBuilder.append("; ");
                }
            }
        }
        return cookieBuilder.toString();
    }

    /**
     * 请求头 Map转Headers
     * @param headersMap
     * @return
     */
    private static Headers parseHeaders(Map<String, String> headersMap) {
        Headers.Builder builder = new Headers.Builder();
        Iterator<Map.Entry<String, String>> iterator = headersMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }
}
