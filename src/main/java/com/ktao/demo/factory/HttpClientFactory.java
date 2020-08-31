package com.ktao.demo.factory;

import com.ktao.demo.interceptor.RequestTraceIdInterceptor;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * okhttp3的Http请求traceId拦截器
 * @author kongtao
 * @version 1.0
 * @description
 * @date 2020/8/28
 */
public class HttpClientFactory {

    private static OkHttpClient client = null;

    static {
        OkHttpClient.Builder ClientBuilder = new OkHttpClient.Builder();
        //读取超时
        ClientBuilder.readTimeout(10, TimeUnit.SECONDS);
        //连接超时
        ClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        //写入超时
        ClientBuilder.writeTimeout(10, TimeUnit.SECONDS);
        client = ClientBuilder
                .addInterceptor(new RequestTraceIdInterceptor())
                .build();
    }

    public static OkHttpClient getHttpClient() {
        return client;
    }
}
