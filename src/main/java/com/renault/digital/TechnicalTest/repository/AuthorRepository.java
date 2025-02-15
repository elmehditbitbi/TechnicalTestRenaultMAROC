package com.renault.digital.TechnicalTest.repository;

import com.renault.digital.TechnicalTest.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorRepository extends JpaRepository<Author, Long> {
}
