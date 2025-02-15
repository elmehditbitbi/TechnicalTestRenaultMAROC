package com.renault.digital.TechnicalTest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookRequest {
    private String title;
    private Long authorId; // Identifiant de l'auteur existant
    private LocalDate publicationDate;
    private String type;
    // Vous pouvez ajouter le champ ISBN si besoin
}
