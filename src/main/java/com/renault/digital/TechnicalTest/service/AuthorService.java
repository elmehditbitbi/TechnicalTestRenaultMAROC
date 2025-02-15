package com.renault.digital.TechnicalTest.service;

import com.renault.digital.TechnicalTest.dto.AuthorRequestDto;
import com.renault.digital.TechnicalTest.model.Author;
import com.renault.digital.TechnicalTest.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author createAuthor(AuthorRequestDto authorRequestDto) {
        log.info("Start Service create a new author with name {}", authorRequestDto.getName());
        Author author = new Author();
        author.setName(authorRequestDto.getName());
        author.setAge(authorRequestDto.getAge());
        author.setFollowersNumber(authorRequestDto.getFollowersNumber());
        log.info("End Service create a new author with name {}", authorRequestDto.getName());
        return authorRepository.save(author);
    }
}
