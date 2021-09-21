package com.vinicius.teste.library_api.service;

import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.model.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerService {

    Customer save(Customer customer);

    Optional<Customer> getById(Long id);

    void delete(Customer customer);

    Customer update(Customer customer);

}
