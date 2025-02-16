package com.renault.digital.TechnicalTest.service;

import com.renault.digital.TechnicalTest.dto.AuthorDTO;
import com.renault.digital.TechnicalTest.dto.AuthorRequestDto;
import com.renault.digital.TechnicalTest.mapper.Mapper;
import com.renault.digital.TechnicalTest.model.Author;
import com.renault.digital.TechnicalTest.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final Mapper mapper;

    public AuthorDTO createAuthor(AuthorRequestDto authorRequestDto) {
        log.info("Start Service create a new author with name {}", authorRequestDto.getName());

        Author author = mapper.authorRequestDtoToAuthor(authorRequestDto);
        Author savedAuthor = authorRepository.save(author);

        log.info("End Service create a new author with name {}", authorRequestDto.getName());
        return mapper.authorToAuthorDTO(savedAuthor);
    }
}
