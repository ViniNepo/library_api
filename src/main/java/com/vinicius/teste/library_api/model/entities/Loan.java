package com.vinicius.teste.library_api.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Loan {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String customer;
    @Column
    private String email;
    @JoinColumn(name = "id_book")
    @ManyToOne
    private Book book;
    @Column
    private LocalDate loanDate;
    @Column
    private Boolean returned;

    public Loan() {
    }

    public Loan(Long id, String customer,
                String email, Book book,
                LocalDate loanDate,
                Boolean returned) {
        this.id = id;
        this.customer = customer;
        this.email = email;
        this.book = book;
        this.loanDate = loanDate;
        this.returned = returned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
