package com.vinicius.teste.library_api.service.impl;

import com.vinicius.teste.library_api.exceptions.BusinessExcepition;
import com.vinicius.teste.library_api.model.dto.LoanFilterDTO;
import com.vinicius.teste.library_api.model.entities.Loan;
import com.vinicius.teste.library_api.repository.LoanRepository;
import com.vinicius.teste.library_api.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class LoanServiceImpl implements LoanService {


    private LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(Loan loan) {
        if(loanRepository.existsByBookNotReturned(loan.getBook())){
            throw new BusinessExcepition("Book ja emprestado");
        }
        return loanRepository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable) {
        return loanRepository.findByBookIsbnOrCustomer(filterDTO.getIsbn(), filterDTO.getCustomer(), pageable);
    }
}
