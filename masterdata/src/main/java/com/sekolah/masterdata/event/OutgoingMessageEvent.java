package com.sekolah.masterdata.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OutgoingMessageEvent {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

}
