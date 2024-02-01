package com.example.demo.controller;

import com.example.demo.model.ClientDTO;
import com.example.demo.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Client")
@RequiredArgsConstructor
public class SaveClientController {

    private final ClientService clientService;

    @PostMapping("save")
    public ResponseEntity<String>saveClient(@RequestBody ClientDTO clientDTO){
        return clientService.saveClient(clientDTO);
    }

}
