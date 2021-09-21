package com.vinicius.teste.library_api.controller;

import com.vinicius.teste.library_api.model.dto.BookDto;
import com.vinicius.teste.library_api.model.dto.CustomerDto;
import com.vinicius.teste.library_api.model.dto.LoanDto;
import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.model.entities.Customer;
import com.vinicius.teste.library_api.model.entities.Loan;
import com.vinicius.teste.library_api.service.BookService;
import com.vinicius.teste.library_api.service.CustomerService;
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
    private CustomerService customerService;
    private LoanService loanService;

    public CustomerController(CustomerService bookService, ModelMapper modelMapper, LoanService loanService) {
        this.customerService = bookService;
        this.modelMapper = modelMapper;
        this.loanService = loanService;
    }

    @GetMapping("/{id}")
    @ApiOperation("Get by ID")
    public CustomerDto get(@PathVariable Long id) {
        return customerService
                .getById(id)
                .map(customer -> modelMapper.map(customer, CustomerDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create a customer")
    public CustomerDto create(@RequestBody @Valid CustomerDto dto) {
        Customer customer = customerService.save(modelMapper.map(dto, Customer.class));
        return modelMapper.map(customer, CustomerDto.class);
    }

    @PutMapping("/{id}")
    @ApiOperation("update a customer")
    public BookDto update(@PathVariable Long id, BookDto dto) {
        return customerService.getById(id).map(book -> {
//            book.setAuthor(dto.getAuthor());
//            book.setTitle(dto.getTitle());
//            book = bookService.update(book);
            return modelMapper.map(book, BookDto.class);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete a customer by ID")
    public void delete(@PathVariable Long id) {
        Customer customer = customerService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));;
        customerService.delete(customer);
    }
}
