package com.renault.digital.TechnicalTest.controller;

import com.renault.digital.TechnicalTest.dto.AuthorRequestDto;
import com.renault.digital.TechnicalTest.model.Author;
import com.renault.digital.TechnicalTest.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
@Tag(name = "Authors", description = "APIs for managing authors")
@Slf4j
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "Create a new author", description = "Creates a new author and returns the created author.")
    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody AuthorRequestDto authorRequestDto) {
        log.info("Start Resource create a new author with name {}", authorRequestDto.getName());
        Author createdAuthor = authorService.createAuthor(authorRequestDto);
        log.info("End Resource create a new author with name {}", authorRequestDto.getName());
        return ResponseEntity.ok(createdAuthor);
    }
}