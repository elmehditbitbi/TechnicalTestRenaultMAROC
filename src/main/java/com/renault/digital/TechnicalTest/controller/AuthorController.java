package com.renault.digital.TechnicalTest.controller;

import com.renault.digital.TechnicalTest.dto.AuthorRequestDto;
import com.renault.digital.TechnicalTest.model.Author;
import com.renault.digital.TechnicalTest.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody AuthorRequestDto authorRequestDto) {
        Author createdAuthor = authorService.createAuthor(authorRequestDto);
        return ResponseEntity.ok(createdAuthor);
    }
}