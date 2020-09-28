package com.vinicius.teste.library_api.repository;

import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.model.entities.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Book createNewBook() {
        return new Book(null, "teste", "teste", "123");
    }

    @Test
    @DisplayName("deve verificar se existe emprestimo nao devolvido para o livro")
    public void existsByBookAndNotReturned() {
        Book book = createNewBook();

        entityManager.persist(book);

        Loan loan = new Loan(null, "teste", "teste@email.com", book, LocalDate.now(), false);

        entityManager.persist(loan);

        boolean exists = loanRepository.existsByBookNotReturned(book);

        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("deve buscar emprestimo pelo isbn do livro ou customer")
    public void findByIsbnOrCustomerTest() {
        Book book = createNewBook();

        entityManager.persist(book);

        Loan loan = new Loan(null, "teste", "teste@email.com", book, LocalDate.now(), false);

        entityManager.persist(loan);

        Page<Loan> result = loanRepository.findByBookIsbnOrCustomer("123", "teste", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(loan);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("deve obter emprestimos com tres dias atras ou nao retornados")
    public void findByLoansDateLessThanAndNotReturned() {
        Book book = createNewBook();

        entityManager.persist(book);

        Loan loan = new Loan(null, "teste", "teste@email.com", book, LocalDate.now().minusDays(5), false);

        entityManager.persist(loan);

        List<Loan> result =  loanRepository.findByLoansDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        assertThat(result).hasSize(1).contains(loan);
    }

    @Test
    @DisplayName("nao deve obter emprestimos com tres dias atras ou nao retornados")
    public void notFindByLoansDateLessThanAndNotReturned() {
        Book book = createNewBook();

        entityManager.persist(book);

        Loan loan = new Loan(null, "teste", "teste@email.com", book, LocalDate.now(), false);

        entityManager.persist(loan);

        List<Loan> result =  loanRepository.findByLoansDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        assertThat(result).isEmpty();
    }
}
