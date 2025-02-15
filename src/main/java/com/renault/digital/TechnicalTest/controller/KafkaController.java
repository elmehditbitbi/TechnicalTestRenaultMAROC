package com.renault.digital.TechnicalTest.controller;

import com.renault.digital.TechnicalTest.dto.MessageDto;
import com.renault.digital.TechnicalTest.service.JsonFileReaderService;
import com.renault.digital.TechnicalTest.service.KafkaProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/kafka")
@Tag(name = "Kafka", description = "APIs for handling Kafka events and messages")
@Slf4j
public class KafkaController {

    private final KafkaProducerService producerService;
    private final JsonFileReaderService jsonFileReaderService;

    public KafkaController(KafkaProducerService producerService, JsonFileReaderService jsonFileReaderService) {
        this.producerService = producerService;
        this.jsonFileReaderService = jsonFileReaderService;
    }
    @Operation(summary = "Send messages to Kafka",
            description = "Reads messages from a JSON file, adds the current date to each message, and sends each message to the 'test' Kafka topic.")
    @PostMapping("/send-messages")
    public String sendMessages() {
        log.info("Start Resource send messages to Kafka");
        try {
            List<MessageDto> messages = jsonFileReaderService.readJsonFile();
            for (MessageDto message : messages) {
                producerService.sendMessage("test", message.toString());
            }
            log.info("End Resource send messages to Kafka");
            return "Messages sent successfully !";
        } catch (IOException e) {
            log.info("End Resource send messages to Kafka with error {}", e.getMessage());
            return "Error reading JSON file : " + e.getMessage();
        }
    }}
