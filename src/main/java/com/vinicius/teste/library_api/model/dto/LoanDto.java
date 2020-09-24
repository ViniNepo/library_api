package com.vinicius.teste.library_api.model.dto;

public class LoanDto {

    private Long id;
    private String isbn;
    private String costumer;
    private BookDto book;

    public LoanDto() {
    }

    public LoanDto(Long id, String isbn, String costumer, BookDto book) {
        this.id = id;
        this.isbn = isbn;
        this.costumer = costumer;
        this.book = book;
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
}
