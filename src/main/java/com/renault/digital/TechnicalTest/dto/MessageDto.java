package com.renault.digital.TechnicalTest.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MessageDto {
    private String name;
    private int age;
    private String gender;
    private LocalDate date;
}