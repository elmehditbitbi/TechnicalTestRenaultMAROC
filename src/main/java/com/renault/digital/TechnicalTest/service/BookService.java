package com.renault.digital.TechnicalTest.service;

import com.renault.digital.TechnicalTest.dto.BookExternalDto;
import com.renault.digital.TechnicalTest.dto.BookRequest;
import com.renault.digital.TechnicalTest.dto.ExternalBooksResponse;
import com.renault.digital.TechnicalTest.exception.ResourceNotFoundException;
import com.renault.digital.TechnicalTest.model.Author;
import com.renault.digital.TechnicalTest.model.Book;
import com.renault.digital.TechnicalTest.repository.AuthorRepository;
import com.renault.digital.TechnicalTest.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    /**
     * 1. Ajouter un nouveau livre.
     */
    public Book addBook(BookRequest bookRequest) {
        Author author = authorRepository.findById(bookRequest.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé avec l'id " + bookRequest.getAuthorId()));
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setPublicationDate(bookRequest.getPublicationDate());
        book.setType(bookRequest.getType());
        // Vous pouvez définir l'ISBN ici si inclus dans le DTO
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    /**
     * 2. Mettre à jour un livre existant.
     */
    public Book updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id " + id));
        book.setTitle(bookRequest.getTitle());
        book.setPublicationDate(bookRequest.getPublicationDate());
        book.setType(bookRequest.getType());
        if(bookRequest.getAuthorId() != null) {
            Author author = authorRepository.findById(bookRequest.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé avec l'id " + bookRequest.getAuthorId()));
            book.setAuthor(author);
        }
        return bookRepository.save(book);
    }

    /**
     * 3. Suppression d'un livre à partir de son id.
     */
    public void deleteBook(Long id) {
        if(!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Livre non trouvé avec l'id " + id);
        }
        bookRepository.deleteById(id);
    }

    /**
     * 4. Obtenir les informations d'un livre à partir de son id.
     */
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id " + id));
    }

    /**
     * 5. Obtenir les informations d'un livre à partir de son nom (ici le titre).
     */
    public Book getBookByTitle(String title) {
        return bookRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec le titre " + title));
    }

    /**
     * 6. Calculer une note sur 10 d’un livre en fonction de sa date de publication
     *    (les livres récents sont mieux notés) et du nombre de followers de l’auteur.
     */
    public double rateBook(Long id) {
        Book book = getBookById(id);
        LocalDate publicationDate = book.getPublicationDate();
        int yearsDiff = Period.between(publicationDate, LocalDate.now()).getYears();

        // Score de récence : exponentiel pour mieux pénaliser les vieux livres
        double recencyScore = 10 * Math.exp(-0.1 * yearsDiff); // Décroissance rapide mais jamais 0

        // Bonus d'auteur : Plus d’influence si l’auteur est populaire
        int followers = book.getAuthor().getFollowersNumber() != null ? book.getAuthor().getFollowersNumber() : 0;
        double authorBonus = Math.min(Math.log(1 + followers / 500.0), 2.0); // Plus doux et progressif

        // Score final plafonné à 10
        double totalScore = Math.min(recencyScore + authorBonus, 10.0);

        return Math.round(totalScore * 100.0) / 100.0;
    }

    /**
     * 7. A partir d'une liste d’IDs de livres, retourner la liste des auteurs sans doublon.
     */
    public List<Author> getAuthorsByBookIds(List<Long> bookIds) {
        List<Book> books = bookRepository.findAllById(bookIds);
        Set<Author> authorsSet = books.stream()
                .map(Book::getAuthor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return new ArrayList<>(authorsSet);
    }

    /**
     * 8. Requête externe : trouver un livre à partir de son code ISBN en appelant une autre API.
     */
    public ExternalBooksResponse getBookByIsbnExternal(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        ExternalBooksResponse externalBooksResponse = new ExternalBooksResponse();
        String url = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&format=json";
        Map<String, BookExternalDto> response = restTemplate.getForObject(url, Map.class);
        externalBooksResponse.setBooks(response);
        return externalBooksResponse;
    }
}
