package com.example.demo.service.impl;

import com.example.demo.entity.Client;
import com.example.demo.model.ClientDTO;
import com.example.demo.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ClientServiceImpl implements ClientService {

    @Override
    public ResponseEntity<String> saveClient(ClientDTO clientDTO) {
            Client client= new Client();
            client.setId(clientDTO.getId());
            client.setTin(clientDTO.getTin());
            client.setName(clientDTO.getName());
            client.setSurname(clientDTO.getSurName());
            client.setPatronymic(clientDTO.getPatronymic());
            client.setDocuments(clientDTO.getDocument());

            return ResponseEntity.status(HttpStatus.OK).body("client save successful");
        }

}
