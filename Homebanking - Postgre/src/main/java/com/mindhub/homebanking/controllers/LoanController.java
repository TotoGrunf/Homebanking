package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static java.util.stream.Collectors.toList;


@RestController
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO ,Authentication authentication) {

        Client currentClient = clientRepository.findByEmail(authentication.getName());

        Account destinyAccount = accountRepository.findByNumber(loanApplicationDTO.getDestinyAccount());

        Loan loanId = loanRepository.findById(loanApplicationDTO.getId());

        if(loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() < 1 || loanApplicationDTO.getDestinyAccount().isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(destinyAccount == null){
            return new ResponseEntity<>("account dosen't exist", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() > loanId.getMaxAmount()){
            return new ResponseEntity<>("Amount limit exceeded", HttpStatus.FORBIDDEN);
        }

        if(!loanId.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Payments not allowed", HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findByNumber(loanApplicationDTO.getDestinyAccount()) == null){
            return new ResponseEntity<>("destiny account is not available", HttpStatus.FORBIDDEN);
        }

        if(!currentClient.getAccounts().contains(accountRepository.findByNumber(destinyAccount.getNumber()))){
            return new ResponseEntity<>("Destiny account do not match with client user", HttpStatus.FORBIDDEN);
        }

        Double plus = (loanApplicationDTO.getAmount() * 0.2) + loanApplicationDTO.getAmount();

        Transaction creditTransaction = new Transaction(CREDIT,loanApplicationDTO.getAmount(),loanId.getName()+ " loan approved", LocalDateTime.now(),destinyAccount);

        transactionRepository.save(creditTransaction);

        destinyAccount.setBalance(destinyAccount.getBalance() + loanApplicationDTO.getAmount());

        ClientLoan loanPetition = new ClientLoan(plus, loanApplicationDTO.getPayments(),currentClient,loanId);
        clientLoanRepository.save(loanPetition);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
    }
}