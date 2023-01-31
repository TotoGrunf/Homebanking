package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAllAccounts().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @GetMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id){

        return new AccountDTO(accountService.getAccountsById(id));
    }


    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> registerAccount(@RequestParam AccountType accountType, Authentication authentication){

        String forAccount = "VIN-" + getForAccount(1000000,99999999);
        Client newCurrentClient = clientService.getClientsByEmail(authentication.getName());

        if (newCurrentClient.getAccounts().size() >= 3){
            return new ResponseEntity<>("You have more than 3 accounts", HttpStatus.FORBIDDEN);
        }
        if (newCurrentClient.getAccounts().stream().filter(account -> account.getAccountType().equals(accountType) && account.getActive()).count() >= 3){
            return new ResponseEntity<>("You can´t create more accounts", HttpStatus.FORBIDDEN);
        }

        accountService.saveAccount(new Account(forAccount, 00.00, LocalDateTime.now(), newCurrentClient, true, accountType ));

        return new ResponseEntity<>(HttpStatus.CREATED);


    }

    @PatchMapping("/api/clients/current/accounts/remove")
    public ResponseEntity<Object> removeAccount (Authentication authentication, @RequestParam long accountId ){

        Client authenticationClient = clientService.getClientsByEmail(authentication.getName());

        Account account = accountService.getAccountsById(accountId);

        if(account == null){
            return new ResponseEntity<>("The account does´t exist", HttpStatus.FORBIDDEN);
        }
        if(authenticationClient == null){
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() > 0){
            return new ResponseEntity<>("you cannot delete this account because it still has an amount greater than $0", HttpStatus.FORBIDDEN);
        }

        account.setActive(false);
        accountService.saveAccount(account);
        return new ResponseEntity<>("Account has been removed successfully", HttpStatus.ACCEPTED);
    }

    public int getForAccount(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}