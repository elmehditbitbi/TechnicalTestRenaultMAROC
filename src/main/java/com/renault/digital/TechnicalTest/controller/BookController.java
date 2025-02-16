package com.renault.digital.TechnicalTest.controller;

import com.renault.digital.TechnicalTest.dto.*;
import com.renault.digital.TechnicalTest.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "APIs for managing books")
@Slf4j
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * Add a new book.
     *
     * @param bookRequest The details of the book to add.
     * @return The created book wrapped in a ResponseEntity.
     */
    @Operation(summary = "Add a new book", description = "Creates a new book and returns the created book.")
    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody BookRequest bookRequest) {
        log.info("Start resource add new book by author id {}", bookRequest.getAuthorId());
        BookDto savedBook = bookService.addBook(bookRequest);
        log.info("End resource add new book by author id {}", bookRequest.getAuthorId());
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    /**
     * Update an existing book.
     *
     * @param id The ID of the book to update.
     * @param bookRequest The updated book details.
     * @return The updated book wrapped in a ResponseEntity.
     */
    @Operation(summary = "Update an existing book", description = "Updates an existing book by its ID and returns the updated book.")
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookRequest bookRequest) {
        log.info("Start resource update an existing book by id {}", id);
        BookDto updatedBook = bookService.updateBook(id, bookRequest);
        log.info("End resource update an existing book by id {}", id);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Delete a book by its ID.
     *
     * @param id The ID of the book to delete.
     * @return A ResponseEntity with no content.
     */
    @Operation(summary = "Delete a book", description = "Deletes a book by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("Start resource delete a book by id {}", id);
        bookService.deleteBook(id);
        log.info("End resource delete a book by id {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieve a book by its ID.
     *
     * @param id The ID of the book to retrieve.
     * @return The book details wrapped in a ResponseEntity.
     */
    @Operation(summary = "Get a book by ID", description = "Retrieves a book by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        log.info("Start resource get a book by ID {}", id);
        BookDto book = bookService.getBookById(id);
        log.info("End resource get a book by ID {}", id);
        return ResponseEntity.ok(book);
    }

    /**
     * Retrieve a book by its title.
     *
     * @param title The title of the book to search for.
     * @return The book details wrapped in a ResponseEntity.
     */
    @Operation(summary = "Get a book by title", description = "Retrieves a book by its title.")
    @GetMapping("/search")
    public ResponseEntity<BookDto> getBookByTitle(@RequestParam String title) {
        log.info("Start Resource get a book by title {}", title);
        BookDto book = bookService.getBookByTitle(title);
        log.info("End Resource get a book by title {}", title);
        return ResponseEntity.ok(book);
    }

    /**
     * Calculate the rating of a book based on its publication date and the popularity of its author.
     *
     * @param id The ID of the book.
     * @return The calculated rating wrapped in a ResponseEntity.
     */
    @Operation(summary = "Rate a book", description = "Calculates the rating of a book based on its publication date and the popularity of its author.")
    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> rateBook(@PathVariable Long id) {
        log.info("Start Resource rate a book with id {}", id);
        double rating = bookService.rateBook(id);
        log.info("End Resource rate a book with id {}", id);
        return ResponseEntity.ok(rating);
    }

    /**
     * Retrieve authors associated with a list of books (without duplicates).
     *
     * @param booksDto A DTO containing a list of book IDs.
     * @return A list of authors wrapped in a ResponseEntity.
     */
    @Operation(summary = "Get authors by book IDs", description = "Retrieves authors associated with a list of books (without duplicates).")
    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDTO>> getAuthorsByBookIds(@RequestBody BooksDto booksDto) {
        log.info("Start Resource get authors by book IDs {}", booksDto.getBookIds());
        List<AuthorDTO> authors = bookService.getAuthorsByBookIds(booksDto.getBookIds());
        log.info("End Resource get authors by book IDs {}", booksDto.getBookIds());
        return ResponseEntity.ok(authors);
    }

    /**
     * Call an external API to retrieve book details using its ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return The external books response wrapped in a ResponseEntity.
     */
    @Operation(summary = "Get book by ISBN externally", description = "Calls an external API to retrieve book details using its ISBN.")
    @GetMapping("/external")
    public ResponseEntity<ExternalBooksResponse> getBookByIsbnExternal(@RequestParam String isbn) {
        log.info("Start Resource Get book by ISBN externally {}", isbn);
        ExternalBooksResponse  response = bookService.getBookByIsbnExternal(isbn);
        log.info("End Resource Get book by ISBN externally {}", isbn);
        return ResponseEntity.ok(response);
    }
}
