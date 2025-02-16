package com.renault.digital.TechnicalTest.mapper;

import com.renault.digital.TechnicalTest.dto.AuthorDTO;
import com.renault.digital.TechnicalTest.dto.AuthorRequestDto;
import com.renault.digital.TechnicalTest.dto.BookDto;
import com.renault.digital.TechnicalTest.dto.BookRequest;
import com.renault.digital.TechnicalTest.model.Author;
import com.renault.digital.TechnicalTest.model.Book;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    @Mapping(source = "author.name", target = "authorName")
    BookDto bookToBookDto(Book book);

    @Mapping(source = "authorId", target = "author.id")
    Book bookRequestToBook(BookRequest bookRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateBookFromRequest(BookRequest bookRequest, @MappingTarget Book book);

    AuthorDTO authorToAuthorDTO(Author author);

    Author authorRequestDtoToAuthor(AuthorRequestDto authorRequestDto);
}
