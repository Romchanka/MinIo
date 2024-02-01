package com.example.demo.service;

import com.example.demo.model.ClientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ClientService {
    ResponseEntity<String> saveClient(ClientDTO clientDTO);
}


