package com.renault.digital.TechnicalTest.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ExternalBooksResponse {
    private Map<String, BookExternalDto> books;
}
