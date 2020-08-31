package com.ktao.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface CustomBatchSpecificRecordHandler<T> {
    void callbackBatch(ConsumerRecords<String, T> records);
}