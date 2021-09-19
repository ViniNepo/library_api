package com.vinicius.teste.library_api.enums;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum TypeOfContactEnum {

    BUSINESS(1, "automatic.debit.registration.current.saving"),
    MOBILE(2, "Collection"),
    HOME(3, "Home");

    private final Integer id;
    private final String description;


    private static final Map<Integer, TypeOfContactEnum> TYPE = new HashMap<>(values().length);
    static {
        TYPE.putAll(stream(values()).collect(toMap(TypeOfContactEnum::getId, identity())));
    }

    TypeOfContactEnum(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription(){
        return description;
    }

    public static TypeOfContactEnum getTypeOfContactEnum(Integer id) {
        return TYPE.get(id);
    }
}
