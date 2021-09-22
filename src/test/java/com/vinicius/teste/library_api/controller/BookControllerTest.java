package com.vinicius.teste.library_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinicius.teste.library_api.exceptions.BusinessExcepition;
import com.vinicius.teste.library_api.model.dto.BookDto;
import com.vinicius.teste.library_api.model.dto.CustomerDto;
import com.vinicius.teste.library_api.model.entities.Address;
import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.model.entities.Contact;
import com.vinicius.teste.library_api.model.entities.Customer;
import com.vinicius.teste.library_api.service.CustomerService;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    static String CUSTOMER_API = "/customer";

    @Autowired
    MockMvc mvc;

    @MockBean
    CustomerService service;

    private Address createAddress() {
        return new Address(1L, "5 avenue", 1, null, "Brazil", "Sao Paulo", "Sao Paulo", "02020000", new Customer());
    }


    private CustomerDto createNewCustomerDto() {
        return new CustomerDto(null, "Joao", "Albulquerque", "11122233344", "11222333x", LocalDate.of(1997, 01, 01), "email@gmail.com", createAddress(), Arrays.asList(), Arrays.asList());
    }

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTest() throws Exception {

        CustomerDto customerDto = createNewCustomerDto();
        Customer customer = new Customer();
        given(service.save((any(Customer.class)))).willReturn(customer);
        String json = new ObjectMapper().writeValueAsString(customerDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CUSTOMER_API)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty());
    }

    @Test
    @DisplayName("Deve lancar erro de validacao nao houver dados suficientes para criacao do livro")
    public void createInvalidTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new BookDto());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CUSTOMER_API)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));
    }

    @Test
    @DisplayName("Deve lancar erro de validacao ao criar um livro com Isbm repetido")
    public void createBookWithRepeatIsbnTest() throws Exception {

        String msg = "Usuario já cadastrado.";
        CustomerDto dto = createNewCustomerDto();
        String json = new ObjectMapper().writeValueAsString(dto);
        given(service.save(any(Customer.class))).willThrow(new BusinessExcepition(msg));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CUSTOMER_API)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(msg));
    }

    @Test
    @DisplayName("pega info dos livros")
    public void getBookDetailTest() throws Exception {

        Long id = 1L;
        Customer customer = new Customer();
        given(service.getById(id)).willReturn(Optional.of(customer));

        mvc.perform(get(CUSTOMER_API.concat("/" + id))
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id));
    }

    @Test
    @DisplayName("Not Found info dos livros")
    public void getBookDetailNotFoundTest() throws Exception {

        given(service.getById(anyLong())).willReturn(Optional.empty());

        mvc.perform(get(CUSTOMER_API.concat("/" + 1)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deve deletar um livro")
    public void deleteBookTest() throws Exception {

        Customer customer = new Customer();
        given(service.getById(anyLong())).willReturn(Optional.of(customer));

        mvc.perform(delete(CUSTOMER_API.concat("/" + 1L)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("deve retornar not found quando nao encontrar o livro para deletar")
    public void deleteBookNotFoundTest() throws Exception {

        given(service.getById(anyLong())).willReturn(Optional.empty());

        mvc.perform(delete(CUSTOMER_API.concat("/" + 1L)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deve atualizar o livro quando encontrar na banco")
    public void updateBookTest() throws Exception {

        Long id = 1L;
        String json = new ObjectMapper().writeValueAsString(createNewCustomerDto());

        Customer updateCustomer = new Customer();
        given(service.getById(id)).willReturn(Optional.of(updateCustomer));

        Customer updatedCustomer = new Customer();
        given(service.update(updateCustomer)).willReturn(updatedCustomer);

        mvc.perform(put(CUSTOMER_API.concat("/" + id))
                .accept(APPLICATION_JSON)
                .content(json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id));
    }

    @Test
    @DisplayName("deve atualizar o livro quando encontrar na banco")
    public void updateBookNotFoundTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(createNewCustomerDto());

        given(service.getById(anyLong())).willReturn(Optional.empty());

        mvc.perform(delete(CUSTOMER_API.concat("/" + 1))
                .accept(APPLICATION_JSON)
                .content(json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
