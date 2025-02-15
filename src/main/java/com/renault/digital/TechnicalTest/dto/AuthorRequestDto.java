package com.renault.digital.TechnicalTest.dto;

import lombok.Data;

@Data
public class AuthorRequestDto {
    private String name;
    private Integer age;
    private Integer followersNumber;
}
