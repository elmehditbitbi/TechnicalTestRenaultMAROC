package com.renault.digital.TechnicalTest.service;

import com.renault.digital.TechnicalTest.dto.AuthorRequestDto;
import com.renault.digital.TechnicalTest.model.Author;
import com.renault.digital.TechnicalTest.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author createAuthor(AuthorRequestDto authorRequestDto) {
        Author author = new Author();
        author.setName(authorRequestDto.getName());
        author.setAge(authorRequestDto.getAge());
        author.setFollowersNumber(authorRequestDto.getFollowersNumber());

        return authorRepository.save(author);
    }
}
