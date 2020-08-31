package com.ktao.demo.kafka;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

@Log4j2
public abstract class AbstractMessageHandler<T> implements CustomBatchSpecificRecordHandler<T> {

    @Override
    public void callbackBatch(ConsumerRecords<String, T> records) {
        if (records.isEmpty()){
            return;
        }
        try{
            handleBatchMessage(records);
        }catch (Exception e){
            //记录异常信息，保证监听线程继续运行
            String topic = records.iterator().next().topic();
            log.error("处理消息出现异常,topic:{}", topic, e);
        }
    }

    /**
     * 批量处理消息
     **/
    protected void handleBatchMessage(ConsumerRecords<String, T> records) throws Exception{
        for (ConsumerRecord<String, T> record : records) {
            //在每次循环时都需要重新处理TraceId
            KafkaTraceIdUtil.setConsumerTraceId((ConsumerRecord<String, Object>) record);
            handleMessage(record);
        }
    }

    /**
     * 处理单个消息
     **/
    protected void handleMessage(ConsumerRecord<String, T> record){

    }

}