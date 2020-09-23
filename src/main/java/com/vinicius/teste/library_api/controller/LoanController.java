package com.vinicius.teste.library_api.controller;

import com.vinicius.teste.library_api.model.dto.LoanDto;
import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.model.entities.Loan;
import com.vinicius.teste.library_api.service.BookService;
import com.vinicius.teste.library_api.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private LoanService loanService;
    private BookService bookService;

    public LoanController(LoanService loanService, BookService bookService) {
        this.loanService = loanService;
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDto dto) {

        Book book = bookService.getByIdIsbn(dto.getIsbn()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found"));

        Loan entity = new Loan(null, "teste", book, LocalDate.now(), false);

        entity = loanService.save(entity);

        return entity.getId();
    }
}
