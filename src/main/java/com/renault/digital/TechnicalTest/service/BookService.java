package com.renault.digital.TechnicalTest.service;

import com.renault.digital.TechnicalTest.dto.BookExternalDto;
import com.renault.digital.TechnicalTest.dto.BookRequest;
import com.renault.digital.TechnicalTest.dto.ExternalBooksResponse;
import com.renault.digital.TechnicalTest.exception.ResourceNotFoundException;
import com.renault.digital.TechnicalTest.model.Author;
import com.renault.digital.TechnicalTest.model.Book;
import com.renault.digital.TechnicalTest.repository.AuthorRepository;
import com.renault.digital.TechnicalTest.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Book addBook(BookRequest bookRequest) {
        log.info("Start service add new book by author id {}", bookRequest.getAuthorId());
        Author author = authorRepository.findById(bookRequest.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found " + bookRequest.getAuthorId()));
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setPublicationDate(bookRequest.getPublicationDate());
        book.setType(bookRequest.getType());
        book.setAuthor(author);
        log.info("End service add new book by author id {}", bookRequest.getAuthorId());
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, BookRequest bookRequest) {
        log.info("Start service update an existing book by id {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with the id " + id));
        book.setTitle(bookRequest.getTitle());
        book.setPublicationDate(bookRequest.getPublicationDate());
        book.setType(bookRequest.getType());
        if(bookRequest.getAuthorId() != null) {
            Author author = authorRepository.findById(bookRequest.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found " + bookRequest.getAuthorId()));
            book.setAuthor(author);
        }
        log.info("End service update an existing book by id {}", id);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        log.info("Start service delete a book by id {}", id);
        if(!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with the id " + id);
        }
        log.info("End service delete a book by id {}", id);
        bookRepository.deleteById(id);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with the id " + id));
    }

    public Book getBookByTitle(String title) {
        return bookRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with the title " + title));
    }

    public double rateBook(Long id) {
        log.info("Start Service rate a book with id {}", id);
        // Retrieve the book using its ID.
        Book book = getBookById(id);

        // Get the publication date of the book.
        LocalDate publicationDate = book.getPublicationDate();

        // Calculate the number of years between the publication date and now.
        int yearsDiff = Period.between(publicationDate, LocalDate.now()).getYears();

        // Calculate the recency score:
        // - Start with a base value of 10.
        // - Apply an exponential decay (using Math.exp) to penalize older books.
        //   This means that the score decreases quickly for older books,
        //   but it will never reach 0.
        double recencyScore = 10 * Math.exp(-0.1 * yearsDiff);

        // Calculate the author bonus:
        // - Retrieve the number of followers for the author.
        // - If the number of followers is null, treat it as 0.
        // - Apply a logarithmic function to provide a gradual bonus based on popularity.
        // - Limit the bonus to a maximum of 2.0 to avoid overly high scores.
        int followers = book.getAuthor().getFollowersNumber() != null ? book.getAuthor().getFollowersNumber() : 0;
        double authorBonus = Math.min(Math.log(1 + followers / 500.0), 2.0);

        // Combine the recency score and the author bonus.
        // Cap the total score at 10 to ensure it doesn't exceed the maximum allowed rating.
        double totalScore = Math.min(recencyScore + authorBonus, 10.0);

        log.info("End Service rate a book with id {}", id);
        // Round the final score to two decimal places before returning it.
        return Math.round(totalScore * 100.0) / 100.0;
    }

    public List<Author> getAuthorsByBookIds(List<Long> bookIds) {
        log.info("Start Service get authors by book IDs {}", bookIds);
        List<Book> books = bookRepository.findAllById(bookIds);
        Set<Author> authorsSet = books.stream()
                .map(Book::getAuthor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        log.info("End Service get authors by book IDs {}", bookIds);
        return new ArrayList<>(authorsSet);
    }

    public ExternalBooksResponse getBookByIsbnExternal(String isbn) {
        log.info("Start Service Get book by ISBN externally {}", isbn);
        RestTemplate restTemplate = new RestTemplate();
        ExternalBooksResponse externalBooksResponse = new ExternalBooksResponse();
        String url = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&format=json";
        Map<String, BookExternalDto> response = restTemplate.getForObject(url, Map.class);
        externalBooksResponse.setBooks(response);
        log.info("End Service Get book by ISBN externally {}", isbn);
        return externalBooksResponse;
    }
}
