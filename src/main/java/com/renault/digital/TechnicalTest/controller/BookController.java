package com.renault.digital.TechnicalTest.controller;

import com.renault.digital.TechnicalTest.dto.BookRequest;
import com.renault.digital.TechnicalTest.dto.BooksDto;
import com.renault.digital.TechnicalTest.dto.ExternalBooksResponse;
import com.renault.digital.TechnicalTest.model.Author;
import com.renault.digital.TechnicalTest.model.Book;
import com.renault.digital.TechnicalTest.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    // 1. Ajouter un nouveau livre
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody BookRequest bookRequest) {
        Book savedBook = bookService.addBook(bookRequest);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    // 2. Mettre à jour un livre existant
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody BookRequest bookRequest) {
        Book updatedBook = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(updatedBook);
    }

    // 3. Supprimer un livre par son id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // 4. Obtenir un livre par son id
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    // 5. Obtenir un livre par son titre
    @GetMapping("/search")
    public ResponseEntity<Book> getBookByTitle(@RequestParam String title) {
        Book book = bookService.getBookByTitle(title);
        return ResponseEntity.ok(book);
    }

    // 6. Calculer la note d’un livre
    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> rateBook(@PathVariable Long id) {
        double rating = bookService.rateBook(id);
        return ResponseEntity.ok(rating);
    }

    // 7. Récupérer les auteurs d'une liste de livres (sans doublon)
    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAuthorsByBookIds(@RequestBody BooksDto booksDto) {
        List<Author> authors = bookService.getAuthorsByBookIds(booksDto.getBookIds());
        return ResponseEntity.ok(authors);
    }

    // 8. Appel à une API externe pour récupérer un livre via son ISBN
    @GetMapping("/external")
    public ResponseEntity<ExternalBooksResponse> getBookByIsbnExternal(@RequestParam String isbn) {
        ExternalBooksResponse  response = bookService.getBookByIsbnExternal(isbn);
        return ResponseEntity.ok(response);
    }
}
