package com.vinicius.teste.library_api.service;

import com.vinicius.teste.library_api.exceptions.BusinessExcepition;
import com.vinicius.teste.library_api.model.dto.LoanFilterDTO;
import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.model.entities.Loan;
import com.vinicius.teste.library_api.repository.LoanRepository;
import com.vinicius.teste.library_api.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    LoanService loanService;

    @MockBean
    private LoanRepository loanRepository;

    @BeforeEach
    public void setUp() {
        this.loanService = new LoanServiceImpl(loanRepository);
    }

    Book book = new Book(1L, "teste", "teste", "123");

    @Test
    @DisplayName("deve salvar um emprestimo")
    public void saveLoan() {
        Loan savingLoan = new Loan(null, "test", "teste@email.com", book, LocalDate.now(), false);
        Loan savedLoan = new Loan(1L, "test", "teste@email.com", book, LocalDate.now(), false);

        when(loanRepository.existsByBookNotReturned(book)).thenReturn(false);
        when(loanRepository.save(savingLoan)).thenReturn(savedLoan);

        Loan loan = loanService.save(savingLoan);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getBook()).isEqualTo(savedLoan.getBook());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
    }

    @Test
    @DisplayName("deve dar erro ao salvar um livro emprestado")
    public void loanedBookTest() {
        Loan savingLoan = new Loan(null, "test", "teste@email.com", book, LocalDate.now(), false);

        when(loanRepository.existsByBookNotReturned(book)).thenReturn(true);

        Throwable exception = catchThrowable(() -> loanService.save(savingLoan));

        assertThat(exception).isInstanceOf(BusinessExcepition.class).hasMessage("Book ja emprestado");

        verify(loanRepository, never()).save(savingLoan);
    }

    @Test
    @DisplayName("Deve obter as informacoes de um emprestimo pelo ID")
    public void getLoanDetaisTest() {
        Long id = 1L;
        Loan loan = new Loan(1L, "test", "teste@email.com", book, LocalDate.now(), false);

        when(loanRepository.findById(id)).thenReturn(Optional.of(loan));

        Optional<Loan> result = loanService.getById(id);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(loan.getId());
        assertThat(result.get().getBook()).isEqualTo(loan.getBook());
        assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());

        verify(loanRepository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar o emprestimo")
    public void updateLoanTest() {
        Loan loan = new Loan(1L, "test", "teste@email.com", book, LocalDate.now(), true);

        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan result = loanService.update(loan);

        assertThat(result.getReturned()).isTrue();

        verify(loanRepository).save(loan);
    }

    @Test
    @DisplayName("deve filtrar um livro existente")
    public void findBookTest() {
        LoanFilterDTO loanFilterDTO = new LoanFilterDTO("123", "teste");
        Loan loan = new Loan(1L, "test", "teste@email.com", book, LocalDate.now(), true);
        List<Loan> list = Arrays.asList(loan);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Loan> page = new PageImpl<>(list, pageRequest, list.size());

        when(this.loanRepository.findByBookIsbnOrCustomer(anyString(), anyString(), any(PageRequest.class))).thenReturn(page);

        Page<Loan> result = loanService.find(loanFilterDTO, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }
}
