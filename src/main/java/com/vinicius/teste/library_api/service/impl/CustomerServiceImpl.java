package com.vinicius.teste.library_api.service.impl;

import com.vinicius.teste.library_api.exceptions.BusinessExcepition;
import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.model.entities.Customer;
import com.vinicius.teste.library_api.repository.BookRepository;
import com.vinicius.teste.library_api.repository.CustomerRepository;
import com.vinicius.teste.library_api.service.BookService;
import com.vinicius.teste.library_api.service.CustomerService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;
import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer save(Customer customer) {
        if(repository.findById(customer.getId()).isPresent()) {
            throw new BusinessExcepition("Customer already exist");
        }

        return this.repository.save(customer);
    }

    @Override
    public Optional<Customer> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(Customer customer) {
        if(customer == null || customer.getId() == null) {
            throw new IllegalArgumentException("Customer or id cant be null");
        }
        this.repository.delete(customer);
    }

    @Override
    public Customer update(Customer customer) {
        if(customer == null || customer.getId() == null) {
            throw new IllegalArgumentException("Customer or id cant be null");
        }
        return this.repository.save(customer);
    }
}
