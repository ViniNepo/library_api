package com.vinicius.teste.library_api.model.entities;

import com.vinicius.teste.library_api.enums.TypeOfContactEnum;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Contact {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private TypeOfContactEnum street;

    @Column
    private String number;

    @ManyToOne()
    @JoinColumn(name = "id_customer")
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeOfContactEnum getStreet() {
        return street;
    }

    public void setStreet(TypeOfContactEnum street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
