package com.vinicius.teste.library_api.service;

import com.vinicius.teste.library_api.exceptions.BusinessExcepition;
import com.vinicius.teste.library_api.model.entities.Book;
import com.vinicius.teste.library_api.repository.BookRepository;
import com.vinicius.teste.library_api.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    private Book createNewBook() {
        return new Book(null, "As asventuras", "Eu mesmo", "001");
    }

    @Test
    @DisplayName("deve salvar o livro")
    public void saveBookTest() {
        Book book = createNewBook();
        Book bookSave = new Book(1L, "As asventuras", "Eu mesmo", "001");

        when(repository.existsByIsbn(anyString())).thenReturn(false);
        when(repository.save(book)).thenReturn(bookSave);

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("001");
        assertThat(savedBook.getTitle()).isEqualTo("As asventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Eu mesmo");
    }

    @Test
    @DisplayName("nao deve salvar com isbm repitido")
    public void shouldNotSaveBookWithDuplicatedIsbmTest() {
        Book book = createNewBook();
        when(repository.existsByIsbn(anyString())).thenReturn(true);

        Throwable exception = catchThrowable(() -> service.save(book));

        assertThat(exception)
                .isInstanceOf(BusinessExcepition.class)
                .hasMessage("Isbn já cadastrado.");

        verify(repository, never()).save(book);
    }

    @Test
    @DisplayName("deve retornar um livro pelo id")
    public void getByIdTest() {
        Long id = 1L;

        Book book = createNewBook();
        book.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("deve retornar vazio ao obter um livro pelo id quando nao exitir na base")
    public void getByIdNotFoundTest() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isFalse();
    }

    @Test
    @DisplayName("deve deletar um livro existente")
    public void deleteTest() {
        Book book = createNewBook();
        book.setId(1L);

        assertDoesNotThrow(() -> this.service.delete(book));

        verify(this.repository).delete(book);
    }

    @Test
    @DisplayName("deve deletar um livro existente")
    public void deleteInvalidBookTest() {
        Book book = new Book();

        assertThrows(IllegalArgumentException.class, () -> this.service.delete(book));

        verify(this.repository, never()).delete(book);
    }

    @Test
    @DisplayName("deve atualizar um livro existente")
    public void updateTest() {
        long id = 1L;
        Book updatingBook = createNewBook();
        updatingBook.setId(id);
        Book updatedBook = createNewBook();
        updatedBook.setId(id);

        when(this.repository.save(updatingBook)).thenReturn(updatedBook);

        Book book = service.update(updatingBook);

        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());

        verify(this.repository).save(updatingBook);
    }

    @Test
    @DisplayName("deve gerar erro ao nao atualizar um livro invalido")
    public void updateInvalidBookTest() {
        Book book = new Book();

        assertThrows(IllegalArgumentException.class, () -> this.service.update(book));

        verify(this.repository, never()).save(book);
    }

    @Test
    @DisplayName("deve filtrar um livro existente")
    public void findBookTest() {
        Book book = createNewBook();
        List<Book> list = Arrays.asList(book);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<Book>(list, pageRequest, 1);

        when(this.repository.findAll(any(Example.class), any(PageRequest.class))).thenReturn(page);

        Page<Book> result = service.find(book, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }
}