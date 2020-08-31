package com.ktao.demo.filter;

import com.ktao.demo.util.TraceIdUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

@Log4j2
@Activate(group = CommonConstants.CONSUMER, order = 1)
public class ConsumerTraceIdFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = MDC.get(TraceIdUtil.TRACE_ID_KEY);
        if(StringUtils.isEmpty(traceId)){
            //如果MDC中没有查到traceId，则生成一个
            traceId = TraceIdUtil.getTraceId();
            MDC.put(TraceIdUtil.TRACE_ID_KEY,traceId);
            //兼容日志记录的requestId
            MDC.put(TraceIdUtil.LOG_REQUEST_ID_KEY,traceId);
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "dubbo consumer: 接口 {}，方法{},MDC中没有查到traceId，重新生成：{}", invocation.getInvoker().getInterface().getName(), invocation.getMethodName(), traceId);
        }else {
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "dubbo consumer: 接口 {}，方法{},从MDC中获取traceId：{}" ,invocation.getInvoker().getInterface().getName(), invocation.getMethodName(), traceId);
        }
        RpcContext.getContext().setAttachment(TraceIdUtil.TRACE_ID_KEY, traceId);
        //实际的rpc调用
        return invoker.invoke(invocation);
    }
}