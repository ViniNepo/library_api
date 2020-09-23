package com.vinicius.teste.library_api.service;

import com.vinicius.teste.library_api.model.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

    Book save(Book book);

    Optional<Book> getById(Long id);

    void delete(Book book);

    Book update(Book book);

    Page find(Book filter, Pageable pageRequest);

    Optional<Book> getByIdIsbn(String isbn);
}
