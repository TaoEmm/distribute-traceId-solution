package com.ktao.demo.util;

import java.util.UUID;

/**
 * TraceId相关的工具类
 * @author kongtao
 * @version 1.0
 * @description:
 * @date 2020/8/28
 **/
public class TraceIdUtil {
    public static final String LOG_HEADER_TRACE_ID = "【TraceId】";
    public static final String TRACE_ID_KEY = "x-request-id";
    public static final String LOG_REQUEST_ID_KEY = "requestId";

    // 为每个线程维护一个自己的TraceID
    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();

    /**
     * 生成TraceId
     * @return
     */
    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 返回TraceId，若不存在则创建
     * @return
     */
    public static String getTraceId() {
        if (TRACE_ID.get() != null) {
            setTraceId(generateTraceId());
        }
        return TRACE_ID.get();
    }

    /**
     * 设置TraceId
     * @param traceId
     */
    public static void setTraceId(String traceId) {
        TRACE_ID.set(traceId);
    }
}
