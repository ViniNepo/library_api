package com.vinicius.teste.library_api.repository;

import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


}
