package com.ktao.demo.interceptor;

import com.ktao.demo.util.TraceIdUtil;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Http Client的拦截器，用来处理TraceId
 * 用于Spring的RestTemplate方式调用
 * @author kongtao
 * @version 1.0
 * @description
 * @date 2020/8/28
 */
@Log4j2
public class HttpClientTraceIdInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        String traceId = MDC.get(TraceIdUtil.TRACE_ID_KEY);
        if (StringUtils.isEmpty(traceId)) {
            traceId = TraceIdUtil.getTraceId();
            MDC.put(TraceIdUtil.TRACE_ID_KEY, traceId);
            // 兼容日志记录的requestId
            MDC.put(TraceIdUtil.LOG_REQUEST_ID_KEY, traceId);
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "HttpClient: 发起请求url:{},MDC没有TraceId，新生成TraceId:{}",request.getURI(),traceId);
        } else {
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID +"HttpClient: 发起请求url:{},从MDC中获取TraceId:{}",request.getURI(),traceId);
        }
        headers.add(TraceIdUtil.TRACE_ID_KEY,traceId);
        return execution.execute(request,body);
    }
}
