package com.vinicius.teste.library_api.model.dto;

import com.vinicius.teste.library_api.model.entities.Address;
import com.vinicius.teste.library_api.model.entities.Contact;
import com.vinicius.teste.library_api.model.entities.Loan;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

public class CustomerDto {

    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String cpf;

    @NotEmpty
    private String rg;

    @NotEmpty
    private LocalDate birthdayDate;

    @NotEmpty
    private String email;

    @NotEmpty
    private Address address;

    private List<Contact> contactList;

    private List<Loan> loanList;

    public CustomerDto(Long id, String firstName, String lastName, String cpf, String rg, LocalDate birthdayDate,
                       String email, Address address, List<Contact> contactList, List<Loan> loanList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cpf = cpf;
        this.rg = rg;
        this.birthdayDate = birthdayDate;
        this.email = email;
        this.address = address;
        this.contactList = contactList;
        this.loanList = loanList;
    }

    public CustomerDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(LocalDate birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public List<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }
}
