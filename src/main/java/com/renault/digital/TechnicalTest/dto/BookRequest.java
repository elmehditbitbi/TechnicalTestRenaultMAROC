package com.renault.digital.TechnicalTest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookRequest {
    private String title;
    private Long authorId;
    private LocalDate publicationDate;
    private String type;
}
