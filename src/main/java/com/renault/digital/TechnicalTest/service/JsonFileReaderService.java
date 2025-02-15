package com.renault.digital.TechnicalTest.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.digital.TechnicalTest.dto.MessageDto;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Service
public class JsonFileReaderService {
    private final ObjectMapper objectMapper;

    public JsonFileReaderService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<MessageDto> readJsonFile() throws IOException {

        ClassPathResource resource = new ClassPathResource("messages.json");
        InputStream inputStream = resource.getInputStream();

        List<MessageDto> messages = objectMapper.readValue(inputStream, new TypeReference<>() {});

        messages.forEach(msg -> msg.setDate(LocalDate.now()));

        return messages;
    }
}
