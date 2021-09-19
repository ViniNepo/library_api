package com.vinicius.teste.library_api.model.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

public class Address {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String street;

    @Column
    private String number;

    @Column
    private String complement;

    @Column
    private LocalDate country;

    @Column
    private String state;

    @Column
    private LocalDate city;

    @Column
    private LocalDate zip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public LocalDate getCountry() {
        return country;
    }

    public void setCountry(LocalDate country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDate getCity() {
        return city;
    }

    public void setCity(LocalDate city) {
        this.city = city;
    }

    public LocalDate getZip() {
        return zip;
    }

    public void setZip(LocalDate zip) {
        this.zip = zip;
    }
}
