package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ClientDTO {
    private Long id;
    private String  tin;
    private String name;
    private String surName;
    private String patronymic;
    private String document;

}
