package com.vinicius.teste.library_api.model.dto;

public class LoanDto {

    private Long id;
    private String isbn;
    private String costumer;
    private BookDto book;
    private String email;

    public LoanDto() {
    }

    public LoanDto(Long id, String isbn, String costumer, BookDto book, String email) {
        this.id = id;
        this.isbn = isbn;
        this.costumer = costumer;
        this.book = book;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCostumer() {
        return costumer;
    }

    public void setCostumer(String costumer) {
        this.costumer = costumer;
    }

    public BookDto getBook() {
        return book;
    }

    public void setBook(BookDto book) {
        this.book = book;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
