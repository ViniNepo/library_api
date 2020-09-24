package com.vinicius.teste.library_api.model.dto;

public class ReturnedLoanDTO {

    private Boolean returned;

    public ReturnedLoanDTO() {
    }

    public ReturnedLoanDTO(Boolean returned) {
        this.returned = returned;
    }

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }
}
