package com.renault.digital.TechnicalTest.repository;

import com.renault.digital.TechnicalTest.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
}