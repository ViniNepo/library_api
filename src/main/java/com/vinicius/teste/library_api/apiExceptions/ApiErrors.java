package com.vinicius.teste.library_api.apiExceptions;

import com.vinicius.teste.library_api.exceptions.BusinessExcepition;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    private List<String> errors;
    
    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public ApiErrors(BusinessExcepition ex) {
        this.errors = Arrays.asList(ex.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
