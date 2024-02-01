package com.example.demo.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor

public class RestResponseConfig<E> {

        private HttpStatus httpStatus;
        private E response;
        private int error;
}

