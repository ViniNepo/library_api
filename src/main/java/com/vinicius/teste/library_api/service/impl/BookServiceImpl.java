package com.vinicius.teste.library_api.service.impl;

import com.vinicius.teste.library_api.exceptions.BusinessExcepition;
import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.repository.BookRepository;
import com.vinicius.teste.library_api.service.BookService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;
import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessExcepition("Isbn já cadastrado.");
        }

        return this.repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(Book book) {
        if(book == null || book.getId() == null) {
            throw new IllegalArgumentException("Book or id cant be null");
        }
        this.repository.delete(book);
    }

    @Override
    public Book update(Book book) {
        if(book == null || book.getId() == null) {
            throw new IllegalArgumentException("Book or id cant be null");
        }
        return this.repository.save(book);
    }

    @Override
    public Page find(Book filter, Pageable pageRequest) {
        Example<Book> example = Example.of(filter, matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(CONTAINING));
        return this.repository.findAll(example, pageRequest);
    }
}
