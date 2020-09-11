package com.vinicius.teste.library_api.repository;

import com.vinicius.teste.library_api.model.entities.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    private Book createNewBook() {
        return new Book(null, "teste", "teste", "123");
    }

    @Test
    @DisplayName("retornar true quando existir um livro com o isbn passado")
    public void returnTrueWhenIsbnExist() {
        //cenario
        String isbn = "123";
        Book book = createNewBook();
        entityManager.persist(book);

        //execucao
        boolean existe = bookRepository.existsByIsbn(isbn);

        //verificacao
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("retornar falso quando nao existir um livro com o isbn passado")
    public void returnFalseWhenIsbnWithoutExistTest() {
        //cenario
        String isbn = "123";

        //execucao
        boolean existe = bookRepository.existsByIsbn(isbn);

        //verificacao
        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("retornar um livro pelo id")
    public void returnTrueGetByIdTest() {
        //cenario
        Book book = createNewBook();
        entityManager.persist(book);

        //execucao
        Optional<Book> foundBook = this.bookRepository.findById(book.getId());

        //verificacao
        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("deve salvar um livro")
    public void saveBookTest() {
        //cenario
        Book book = createNewBook();

        //execucao
        Book savedBook = this.bookRepository.save(book);

        //verificacao
        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("deve deletar um livro")
    public void deleteBookTest() {
        //cenario
        Book book = createNewBook();
        entityManager.persist(book);

        //execucao
        Book searchBook = entityManager.find(Book.class, book.getId());
        this.bookRepository.delete(searchBook);

        Book deletedBook = entityManager.find(Book.class, book.getId());

        //verificacao
        assertThat(deletedBook).isNull();
    }
}
