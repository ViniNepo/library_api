package com.vinicius.teste.library_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinicius.teste.library_api.exceptions.BusinessExcepition;
import com.vinicius.teste.library_api.model.dto.BookDto;
import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.service.BookService;
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

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {

    static String BOOK_API = "/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService service;

    private BookDto createNewBookDto() {
        return new BookDto(null, "title", "author", "001");
    }

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTest() throws Exception {

        BookDto bookDto = createNewBookDto();
        Book book = new Book(10L, "title", "author", "001");
        given(service.save((any(Book.class)))).willReturn(book);
        String json = new ObjectMapper().writeValueAsString(bookDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(bookDto.getTitle()))
                .andExpect(jsonPath("author").value(bookDto.getAuthor()))
                .andExpect(jsonPath("isbn").value(bookDto.getIsbn()));
    }

    @Test
    @DisplayName("Deve lancar erro de validacao nao houver dados suficientes para criacao do livro")
    public void createInvalidTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new BookDto());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
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

        String msg = "Isbn já cadastrado.";
        BookDto dto = createNewBookDto();
        String json = new ObjectMapper().writeValueAsString(dto);
        given(service.save(any(Book.class))).willThrow(new BusinessExcepition(msg));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
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
        Book book = new Book(1L, createNewBookDto().getTitle(), createNewBookDto().getAuthor(), createNewBookDto().getIsbn());
        given(service.getById(id)).willReturn(Optional.of(book));

        mvc.perform(get(BOOK_API.concat("/" + id))
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(createNewBookDto().getTitle()))
                .andExpect(jsonPath("author").value(createNewBookDto().getAuthor()))
                .andExpect(jsonPath("isbn").value(createNewBookDto().getIsbn()));
    }

    @Test
    @DisplayName("Not Found info dos livros")
    public void getBookDetailNotFoundTest() throws Exception {

        given(service.getById(anyLong())).willReturn(Optional.empty());

        mvc.perform(get(BOOK_API.concat("/" + 1)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deve deletar um livro")
    public void deleteBookTest() throws Exception {

        Book book = new Book(1L, createNewBookDto().getTitle(), createNewBookDto().getAuthor(), createNewBookDto().getIsbn());
        given(service.getById(anyLong())).willReturn(Optional.of(book));

        mvc.perform(delete(BOOK_API.concat("/" + 1L)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("deve retornar not found quando nao encontrar o livro para deletar")
    public void deleteBookNotFoundTest() throws Exception {

        given(service.getById(anyLong())).willReturn(Optional.empty());

        mvc.perform(delete(BOOK_API.concat("/" + 1L)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deve atualizar o livro quando encontrar na banco")
    public void updateBookTest() throws Exception {

        Long id = 1L;
        String json = new ObjectMapper().writeValueAsString(createNewBookDto());

        Book updateBook = new Book(1L, createNewBookDto().getTitle(), createNewBookDto().getAuthor(), createNewBookDto().getIsbn());
        given(service.getById(id)).willReturn(Optional.of(updateBook));

        Book updatedBook = new Book(id, "teste", "teste", createNewBookDto().getIsbn());
        given(service.update(updateBook)).willReturn(updatedBook);

        mvc.perform(put(BOOK_API.concat("/" + id))
                .accept(APPLICATION_JSON)
                .content(json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(updatedBook.getTitle()))
                .andExpect(jsonPath("author").value(updatedBook.getAuthor()))
                .andExpect(jsonPath("isbn").value(updatedBook.getIsbn()));
    }

    @Test
    @DisplayName("deve atualizar o livro quando encontrar na banco")
    public void updateBookNotFoundTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(createNewBookDto());

        given(service.getById(anyLong())).willReturn(Optional.empty());

        mvc.perform(delete(BOOK_API.concat("/" + 1))
                .accept(APPLICATION_JSON)
                .content(json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve encontrar um livro")
    public void findBookTest() throws Exception {
        Long id = 1L;
        Book book = new Book(id, "teste", "teste", "123");

        given(service.find(any(Book.class), any(Pageable.class)))
                .willReturn(new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0, 100), 1));

        String queryString = String.format("?title=%s&author=%s&page=0&size=100", book.getTitle(), book.getAuthor());

        mvc.perform(get(BOOK_API.concat(queryString))
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }
}
