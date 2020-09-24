package com.vinicius.teste.library_api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinicius.teste.library_api.exceptions.BusinessExcepition;
import com.vinicius.teste.library_api.model.dto.BookDto;
import com.vinicius.teste.library_api.model.dto.LoanDto;
import com.vinicius.teste.library_api.model.dto.LoanFilterDTO;
import com.vinicius.teste.library_api.model.dto.ReturnedLoanDTO;
import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.model.entities.Loan;
import com.vinicius.teste.library_api.service.BookService;
import com.vinicius.teste.library_api.service.LoanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
public class LoanControllerTest {

    static final String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private LoanService loanService;

    @Test
    @DisplayName("Deve realizar emprestimo")
    public void createLoanTest() throws Exception {

        Book book = new Book(1L, "teste", "teste", "123");
        Loan loan = new Loan(1L, "teste", book, LocalDate.now(), false);
        LoanDto dto = new LoanDto(1L, "123", "Carinha", new BookDto());
        String json = new ObjectMapper().writeValueAsString(dto);

        given(bookService.getByIdIsbn("123")).willReturn(Optional.of(book));
        given(loanService.save(any(Loan.class))).willReturn(loan);

        mvc.perform(post(LOAN_API)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated())
            .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar fazer emprestimo de livro inexistente")
    public void invalidIsbnCreateLoanTest() throws Exception {

        LoanDto dto = new LoanDto(1L, "123", "Carinha", new BookDto());
        String json = new ObjectMapper().writeValueAsString(dto);

        given(bookService.getByIdIsbn("123")).willReturn(Optional.empty());

        mvc.perform(post(LOAN_API)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Book not found"));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar fazer emprestimo de livro emprestado")
    public void loanBookErrorOnCreateLoanTest() throws Exception {

        LoanDto dto = new LoanDto(1L, "123", "Carinha", new BookDto());
        Book book = new Book(1L, "teste", "teste", "123");
        String json = new ObjectMapper().writeValueAsString(dto);

        given(bookService.getByIdIsbn("123")).willReturn(Optional.of(book));
        given(loanService.save(any(Loan.class))).willThrow(new BusinessExcepition("Livro emprestado"));

        mvc.perform(post(LOAN_API)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Livro emprestado"));
    }

    @Test
    @DisplayName("deve retornar um livro")
    public void returnBookTest() throws Exception {
        ReturnedLoanDTO dto = new ReturnedLoanDTO(true);
        Loan loan = new Loan(1L, "teste", new Book(), LocalDate.now(), false);
        String json = new ObjectMapper().writeValueAsString(dto);

        given(loanService.getById(anyLong())).willReturn(Optional.of(loan));

        mvc.perform(patch(LOAN_API.concat("/1"))
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());

        verify(loanService, times(1)).update(loan);
    }

    @Test
    @DisplayName("deve retornar erro ao devolver um livro inexistente um livro")
    public void returnInvalidBookTest() throws Exception {
        ReturnedLoanDTO dto = new ReturnedLoanDTO(true);
        String json = new ObjectMapper().writeValueAsString(dto);

        given(loanService.getById(anyLong())).willReturn(Optional.empty());

        mvc.perform(patch(LOAN_API.concat("/1"))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve filtrar emprestimos")
    public void findLoanTest() throws Exception {
        Loan loan = new Loan(1L, "teste", new Book(1L, "teste", "teste", "123"), LocalDate.now(), false);

        given(loanService.find(any(LoanFilterDTO.class), any(Pageable.class)))
                .willReturn(new PageImpl<Loan>(Arrays.asList(loan), PageRequest.of(0, 10), 1));

        String queryString = String.format("?isbn=%s&customer=%s&page=0&size=10", loan.getBook().getIsbn(), loan.getCustomer());

        mvc.perform(get(LOAN_API.concat(queryString))
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(10))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }
}
