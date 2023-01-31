package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;


@RestController
public class ClientController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/api/clients")
    public List<ClientDTO> getClients(){
        return clientService.getAllClients().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @GetMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientService.getClientsById(id));
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientService.getClientsByEmail(email) != null) {
            return new ResponseEntity<>("email already in use", HttpStatus.FORBIDDEN);
        }

        String accountNumber = "VIN-" + getRandomNumber(1000000, 99999999);

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));

        clientService.saveClient(newClient);

        accountService.saveAccount(new Account(accountNumber, 0.0, LocalDateTime.now(), newClient, true, AccountType.CURRENT));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


        @GetMapping("/api/clients/current")
    public ClientDTO getClientsCurrent(Authentication authentication) {
        return new ClientDTO(clientService.getClientsByEmail(authentication.getName()));

    }
    public int getRandomNumber(int min, int max) {return (int) ((Math.random() * (max - min)) + min);}
}
