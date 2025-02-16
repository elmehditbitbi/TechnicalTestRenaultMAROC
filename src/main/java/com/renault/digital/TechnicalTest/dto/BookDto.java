package com.renault.digital.TechnicalTest.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookDto {
    private Long id;
    private String title;
    private LocalDate publicationDate;
    private String type;
    private String authorName;
}
