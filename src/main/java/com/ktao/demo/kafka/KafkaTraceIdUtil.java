package com.ktao.demo.kafka;

import com.ktao.demo.util.TraceIdUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Iterator;

@Log4j2
class KafkaTraceIdUtil {

    public static void setConsumerTraceId(ConsumerRecord<String, Object> record){
        Iterator<Header> iterator = record.headers().iterator();
        String traceId = null;
        while (iterator.hasNext()){
            Header header = iterator.next();
            String key = header.key();
            if (TraceIdUtil.TRACE_ID_KEY.equals(key)){
                traceId = Arrays.toString(header.value());
                break;
            }
        }
        String topic = record.topic();
        boolean isNewTraceId = false;
        if(StringUtils.isEmpty(traceId)){
            //如果消息Header中没有查到traceId，则生成一个
            traceId = TraceIdUtil.generateTraceId();
            TraceIdUtil.setTraceId(traceId);
            isNewTraceId = true;
        }
        MDC.put(TraceIdUtil.TRACE_ID_KEY,traceId);
        //兼容日志记录的requestId
        MDC.put(TraceIdUtil.LOG_REQUEST_ID_KEY,traceId);
        if (isNewTraceId){
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "kafka consumer: topic: {},从Header中没有查到traceId，重新生成{}" , topic,traceId);
        }else {
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "kafka consumer: topic: {},从Header中取到traceId {}" , topic,traceId);
        }
    }
}