package com.ktao.demo.interceptor;

import com.ktao.demo.util.TraceIdUtil;
import lombok.extern.log4j.Log4j2;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * okhttp3的Http请求traceId拦截器
 * @author kongtao
 * @version 1.0
 * @description
 * @date 2020/8/28
 */
@Log4j2
public class RequestTraceIdInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String traceId = MDC.get(TraceIdUtil.TRACE_ID_KEY);
        if (StringUtils.isEmpty(traceId)) {
            traceId = TraceIdUtil.getTraceId();
            MDC.put(TraceIdUtil.TRACE_ID_KEY,traceId);
            // 兼容日志记录的requestId
            MDC.put(TraceIdUtil.LOG_REQUEST_ID_KEY,traceId);
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "HttpClient: 发起请求url:{},MDC没有TraceId，新生成TraceId:{}",originalRequest.url(),traceId);
        }else {
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "HttpClient: 发起请求url:{},从MDC中获取TraceId:{}",originalRequest.url(),traceId);
        }
        Request requestWithTraceId = originalRequest.newBuilder().addHeader(TraceIdUtil.TRACE_ID_KEY, traceId).build();
        return chain.proceed(requestWithTraceId);
    }
}
