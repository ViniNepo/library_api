package com.vinicius.teste.library_api.service;

import com.vinicius.teste.library_api.model.entities.Loan;
import org.springframework.stereotype.Service;

@Service
public interface LoanService {

    Loan save(Loan loan);

}
