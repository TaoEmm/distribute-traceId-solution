package com.ktao.demo.interceptor;

import com.ktao.demo.annotation.TraceId;
import com.ktao.demo.util.TraceIdUtil;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
public class HttpServerTraceIdInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上的注解
            TraceId traceIdAnnotation = handlerMethod.getMethod().getAnnotation(TraceId.class);
            //如果方法上的注解为空，则获取类的注解
            if (traceIdAnnotation == null) {
                traceIdAnnotation = handlerMethod.getMethod().getDeclaringClass().getAnnotation(TraceId.class);
            }
            // 如果没有traceId注解，则返回
            if (traceIdAnnotation == null) {
                return true;
            }
            setTraceId(request);
        }
        return true;
    }

    private void setTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(TraceIdUtil.TRACE_ID_KEY);
        String requestUrl = request.getRequestURL().toString();
        boolean isNewTraceId = false;
        if (StringUtils.isEmpty(traceId)){
            traceId = TraceIdUtil.getTraceId();
            isNewTraceId = true;
        }
        MDC.put(TraceIdUtil.TRACE_ID_KEY,traceId);
        //兼容日志记录的requestId
        MDC.put(TraceIdUtil.LOG_REQUEST_ID_KEY,traceId);
        if (isNewTraceId){
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "HttpServer: 收到请求url:{},header没有设置TraceId，新生成TraceId:{}",requestUrl,traceId);
        }else {
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "HttpServer: 收到请求url:{},从header中取得TraceId:{}",requestUrl,traceId);
        }
    }
}