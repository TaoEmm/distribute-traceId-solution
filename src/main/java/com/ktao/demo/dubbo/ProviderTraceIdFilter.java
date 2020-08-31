package com.ktao.demo.dubbo;

import com.ktao.demo.util.TraceIdUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

/**
 * Provider端
 */
@Log4j2
@Activate(group = CommonConstants.PROVIDER, order = 1)
public class ProviderTraceIdFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment(TraceIdUtil.TRACE_ID_KEY);
        // 是否需要生成新TraceId
        boolean isNewTraceId = false;
        if (StringUtils.isEmpty(traceId)) {
            traceId = TraceIdUtil.getTraceId();
            isNewTraceId = true;
            // 交互前重新设置traceId，避免信息丢失
            RpcContext.getContext().setAttachment(TraceIdUtil.TRACE_ID_KEY, traceId);
        }
        MDC.put(TraceIdUtil.TRACE_ID_KEY,traceId);
        //兼容日志记录的requestId
        MDC.put(TraceIdUtil.LOG_REQUEST_ID_KEY,traceId);
        if (isNewTraceId){
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "dubbo provider: 接口 {}，方法{},RpcContext没有设置TraceId，新生成:{}",invocation.getInvoker().getInterface().getName(), invocation.getMethodName(),traceId);
        }else {
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "dubbo provider: 接口 {}，方法{},从RpcContext中获取TraceId：{}",invocation.getInvoker().getInterface().getName(), invocation.getMethodName(),traceId);
        }
        //实际的rpc调用
        return invoker.invoke(invocation);
    }
}
