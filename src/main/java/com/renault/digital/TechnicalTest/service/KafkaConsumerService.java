package com.renault.digital.TechnicalTest.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "test", groupId = "my-group")
    public void listen(String message) {
        System.out.println("Message received : " + message);
    }
}