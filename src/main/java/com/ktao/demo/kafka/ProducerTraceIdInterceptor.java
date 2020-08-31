package com.ktao.demo.kafka;

import com.ktao.demo.util.TraceIdUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Producer端的TraceId拦截器，在发送消息时处理TraceId
 */
@Log4j2
public class ProducerTraceIdInterceptor implements ProducerInterceptor<String, SpecificRecordBase> {
    @Override
    public ProducerRecord<String, SpecificRecordBase> onSend(ProducerRecord<String, SpecificRecordBase> record) {
        String traceId = MDC.get(TraceIdUtil.TRACE_ID_KEY);
        String topic = record.topic();
        if(StringUtils.isEmpty(traceId)){
            //如果MDC中没有查到traceId，则生成一个
            traceId = TraceIdUtil.getTraceId();
            MDC.put(TraceIdUtil.TRACE_ID_KEY,traceId);
            //兼容日志记录的requestId
            MDC.put(TraceIdUtil.LOG_REQUEST_ID_KEY,traceId);
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "kafka Producer: topic: {},MDC中没有查到traceId，重新生成{}" ,topic,traceId);
        }else {
            log.info(TraceIdUtil.LOG_HEADER_TRACE_ID + "kafka Producer: topic: {},从MDC中查到traceId {}" ,topic,traceId);
        }
        record.headers().add(TraceIdUtil.TRACE_ID_KEY,traceId.getBytes());
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}