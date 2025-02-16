package com.renault.digital.TechnicalTest.service;

import com.renault.digital.TechnicalTest.dto.*;
import com.renault.digital.TechnicalTest.dto.BookExternalDto;
import com.renault.digital.TechnicalTest.dto.ExternalBooksResponse;
import com.renault.digital.TechnicalTest.exception.ResourceNotFoundException;
import com.renault.digital.TechnicalTest.mapper.Mapper;
import com.renault.digital.TechnicalTest.model.Author;
import com.renault.digital.TechnicalTest.model.Book;
import com.renault.digital.TechnicalTest.repository.AuthorRepository;
import com.renault.digital.TechnicalTest.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private BookService bookService;

    private BookRequest bookRequest;
    private Book book;
    private Author author;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book();
        book.setId(1L);
        book.setTitle("Les Misérables");
        author = new Author();
        author.setId(2L);
        author.setName("elmehdi tbitbi");
        bookRequest = new BookRequest();
        bookRequest.setTitle("le dernier jour d'un condamné");
        bookRequest.setAuthorId(2L);
        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("le dernier jour d'un condamné");
    }

    @Test
    void addBook_success() {
        when(authorRepository.findById(bookRequest.getAuthorId())).thenReturn(Optional.of(author));
        when(mapper.bookRequestToBook(bookRequest)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(mapper.bookToBookDto(book)).thenReturn(bookDto);
        BookDto result = bookService.addBook(bookRequest);
        assertNotNull(result);
        assertEquals("le dernier jour d'un condamné", result.getTitle());
        verify(authorRepository, times(1)).findById(bookRequest.getAuthorId());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void updateBook_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(bookRequest.getAuthorId())).thenReturn(Optional.of(author));
        doAnswer(invocation -> {
            BookRequest req = invocation.getArgument(0);
            Book b = invocation.getArgument(1);
            b.setTitle(req.getTitle());
            return null;
        }).when(mapper).updateBookFromRequest(bookRequest, book);
        when(bookRepository.save(book)).thenReturn(book);
        when(mapper.bookToBookDto(book)).thenReturn(bookDto);
        BookDto result = bookService.updateBook(1L, bookRequest);
        assertNotNull(result);
        assertEquals("le dernier jour d'un condamné", result.getTitle());
        verify(bookRepository).findById(1L);
        verify(authorRepository).findById(bookRequest.getAuthorId());
        verify(bookRepository).save(book);
    }

    @Test
    void updateBook_bookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBook(1L, bookRequest);
        });
        assertEquals("Book not found with the id 1", exception.getMessage());
        verify(bookRepository).findById(1L);
    }

    @Test
    void updateBook_authorNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(mapper).updateBookFromRequest(bookRequest, book);
        when(authorRepository.findById(bookRequest.getAuthorId())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBook(1L, bookRequest);
        });
        assertEquals("Author not found " + bookRequest.getAuthorId(), exception.getMessage());
        verify(authorRepository).findById(bookRequest.getAuthorId());
    }

    @Test
    void deleteBook_success() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_bookNotFound() {
        when(bookRepository.existsById(1L)).thenReturn(false);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(1L);
        });
        assertEquals("Book not found with the id 1", exception.getMessage());
        verify(bookRepository, times(1)).existsById(1L);
    }

    @Test
    void getBookById_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(mapper.bookToBookDto(book)).thenReturn(bookDto);
        BookDto result = bookService.getBookById(1L);
        assertNotNull(result);
        assertEquals(bookDto.getTitle(), result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_bookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });
        assertEquals("Book not found with the id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookByTitle_success() {
        String title = "la boite a merveille";
        when(bookRepository.findByTitle(title)).thenReturn(Optional.of(book));
        when(mapper.bookToBookDto(book)).thenReturn(bookDto);
        BookDto result = bookService.getBookByTitle(title);
        assertNotNull(result);
        assertEquals(bookDto.getTitle(), result.getTitle());
        verify(bookRepository, times(1)).findByTitle(title);
    }

    @Test
    void getBookByTitle_bookNotFound() {
        String title = "inexistant";
        when(bookRepository.findByTitle(title)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookByTitle(title);
        });
        assertEquals("Book not found with the title " + title, exception.getMessage());
        verify(bookRepository, times(1)).findByTitle(title);
    }

    @Test
    void rateBook_success() {
        Book rateBook = new Book();
        rateBook.setId(100L);
        rateBook.setTitle("Les Misérables");
        rateBook.setPublicationDate(LocalDate.now().minusYears(5));
        Author rateAuthor = new Author();
        rateAuthor.setId(50L);
        rateAuthor.setName("aicha baamrane");
        rateAuthor.setFollowersNumber(1000);
        rateBook.setAuthor(rateAuthor);
        when(bookRepository.findById(100L)).thenReturn(Optional.of(rateBook));
        double result = bookService.rateBook(100L);
        double expectedRecency = 10 * Math.exp(-0.1 * 5);
        double expectedBonus = Math.min(Math.log(1 + 1000 / 500.0), 2.0);
        double expectedTotal = Math.min(expectedRecency + expectedBonus, 10.0);
        double expectedScore = Math.round(expectedTotal * 100.0) / 100.0;
        assertEquals(expectedScore, result);
        verify(bookRepository).findById(100L);
    }

    @Test
    void getAuthorsByBookIds_success() {
        List<Long> ids = Arrays.asList(1L, 2L);
        Book book1 = new Book();
        book1.setId(1L);
        Author author1 = new Author();
        author1.setId(10L);
        author1.setName("elmehdi tbitbi");
        book1.setAuthor(author1);
        Book book2 = new Book();
        book2.setId(2L);
        Author author2 = new Author();
        author2.setId(20L);
        author2.setName("hassan tbitbi");
        book2.setAuthor(author2);
        Book book3 = new Book();
        book3.setId(3L);
        Author author3 = new Author();
        author3.setId(30L);
        author3.setName("mouhssine");
        book3.setAuthor(author3);
        List<Book> books = Arrays.asList(book1, book2, book3);
        when(bookRepository.findAllById(ids)).thenReturn(books);
        AuthorDTO authorDTO1 = new AuthorDTO();
        authorDTO1.setId(10L);
        authorDTO1.setName("elmehdi tbitbi");
        AuthorDTO authorDTO2 = new AuthorDTO();
        authorDTO2.setId(20L);
        authorDTO2.setName("hassan tbitbi");
        AuthorDTO authorDTO3 = new AuthorDTO();
        authorDTO3.setId(30L);
        authorDTO3.setName("mouhssine");
        when(mapper.authorToAuthorDTO(author1)).thenReturn(authorDTO1);
        when(mapper.authorToAuthorDTO(author2)).thenReturn(authorDTO2);
        when(mapper.authorToAuthorDTO(author3)).thenReturn(authorDTO3);
        List<AuthorDTO> result = bookService.getAuthorsByBookIds(ids);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(authorDTO1));
        assertTrue(result.contains(authorDTO2));
        assertTrue(result.contains(authorDTO3));
        verify(bookRepository).findAllById(ids);
    }

    @Test
    void getBookByIsbnExternal_success() {
        String isbn = "1234567890";
        Map<String, BookExternalDto> fakeResponse = new HashMap<>();
        BookExternalDto bookExternalDto = new BookExternalDto();
        fakeResponse.put("ISBN:" + isbn, bookExternalDto);
        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    when(mock.getForObject(anyString(), eq(Map.class))).thenReturn(fakeResponse);
                })) {
            ExternalBooksResponse response = bookService.getBookByIsbnExternal(isbn);
            assertNotNull(response);
            assertEquals(fakeResponse, response.getBooks());
        }
    }
}