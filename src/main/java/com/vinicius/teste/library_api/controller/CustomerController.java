package com.vinicius.teste.library_api.controller;

import com.vinicius.teste.library_api.model.dto.BookDto;
import com.vinicius.teste.library_api.model.dto.LoanDto;
import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.model.entities.Loan;
import com.vinicius.teste.library_api.service.BookService;
import com.vinicius.teste.library_api.service.LoanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
@Api("Customer API")
public class CustomerController {

    private ModelMapper modelMapper;
    private BookService bookService;
    private LoanService loanService;

    public CustomerController(BookService bookService, ModelMapper modelMapper, LoanService loanService) {
        this.bookService = bookService;
        this.modelMapper = modelMapper;
        this.loanService = loanService;
    }

    @GetMapping("/{id}")
    @ApiOperation("Get by ID")
    public BookDto get(@PathVariable Long id) {
        return bookService
                .getById(id)
                .map(book -> modelMapper.map(book, BookDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @ApiOperation("Get all books")
    public Page<BookDto> find(BookDto dto, Pageable pageable) {
        Book filter = modelMapper.map(dto, Book.class);
        Page<Book> result = this.bookService.find(filter, pageable);
        List<BookDto> list = result.getContent().stream()
                .map(entity ->  modelMapper.map(entity, BookDto.class))
                .collect(Collectors.toList());
        return new PageImpl<BookDto>(list, pageable, result.getTotalElements());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create a book")
    public BookDto create(@RequestBody @Valid BookDto dto) {
        Book book = bookService.save(modelMapper.map(dto, Book.class));
        return modelMapper.map(book, BookDto.class);
    }

    @PutMapping("/{id}")
    @ApiOperation("update a book")
    public BookDto update(@PathVariable Long id, BookDto dto) {
        return bookService.getById(id).map(book -> {
            book.setAuthor(dto.getAuthor());
            book.setTitle(dto.getTitle());
            book = bookService.update(book);
            return modelMapper.map(book, BookDto.class);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete a book by ID")
    public void delete(@PathVariable Long id) {
        Book book = bookService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));;
        bookService.delete(book);
    }

    @GetMapping("{id}/loans")
    @ApiOperation("Get loans by ID")
    public Page<LoanDto> loanByBook(@PathVariable Long id, Pageable pageable) {
        Book book = bookService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Page<Loan> result = loanService.getLoansByBook(book, pageable);
        List<LoanDto> list = result.getContent().stream().map(loan -> {
            Book loanBook = loan.getBook();
            BookDto bookDto = modelMapper.map(loanBook, BookDto.class);
            LoanDto loanDto = modelMapper.map(loan, LoanDto.class);
            loanDto.setBook(bookDto);
            return loanDto;
        }).collect(Collectors.toList());

        return new PageImpl<LoanDto>(list, pageable, result.getTotalElements());
    }
}
