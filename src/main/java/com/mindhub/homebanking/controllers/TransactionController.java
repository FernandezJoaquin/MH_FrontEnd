package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepo;

    @Autowired
    AccountRepository accountRepo;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> makeTransaction(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam Double amount, @RequestParam String description,
                                                  Authentication authentication){
        if(accountRepo.findByNumber(fromAccountNumber).getClient().getEmail() != authentication.getName()){
            return new ResponseEntity<>("You don't own the sending account", HttpStatus.FORBIDDEN);
        }
        if(accountRepo.findByNumber(toAccountNumber) == null){
            return new ResponseEntity<>("Receiving account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(amount.isNaN() || amount == 0){
            return new ResponseEntity<>("Amount field is incomplete", HttpStatus.FORBIDDEN);
        }
        if(description.isBlank()){
            return  new ResponseEntity<>("Description field is empty",HttpStatus.FORBIDDEN);
        }
        if(Objects.equals(fromAccountNumber, toAccountNumber)){
            return new ResponseEntity<>("Sender and receiver are the same", HttpStatus.FORBIDDEN);
        }
        if(accountRepo.findByNumber(fromAccountNumber).getBalance() < amount){
            return new ResponseEntity<>("Stated amount is greater than the account's balance", HttpStatus.FORBIDDEN);
        }
        Transaction transactionStart = new Transaction(TransactionType.DEBIT, amount, accountRepo.findByNumber(toAccountNumber).getNumber()+":"+description);
        Transaction transactionEnd = new Transaction(TransactionType.CREDIT, amount, accountRepo.findByNumber(fromAccountNumber).getNumber()+":"+description);
        accountRepo.findByNumber(fromAccountNumber).addTransactions(transactionStart);
        accountRepo.findByNumber(toAccountNumber).addTransactions(transactionEnd);
        transactionRepo.save(transactionEnd);
        transactionRepo.save(transactionStart);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
