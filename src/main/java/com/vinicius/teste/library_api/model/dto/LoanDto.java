package com.vinicius.teste.library_api.model.dto;

public class LoanDto {

    private String isbn;
    private String costumer;

    public LoanDto() {
    }

    public LoanDto(String isbn, String costumer) {
        this.isbn = isbn;
        this.costumer = costumer;
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
}
