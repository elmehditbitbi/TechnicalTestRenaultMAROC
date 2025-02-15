package com.renault.digital.TechnicalTest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private Integer followersNumber;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Book> books = new ArrayList<>();
}
