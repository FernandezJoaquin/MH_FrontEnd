package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
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
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> makeTransaction(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                                  @RequestParam Double amount, @RequestParam String description,
                                                  Authentication authentication){
        if(clientService.getClient(authentication.getName()) == null){
            return new ResponseEntity<>("You aren't a registered client", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.getClient(authentication.getName());
        if(fromAccountNumber.isBlank() || accountService.getAccountByNumber(fromAccountNumber) == null){
            return new ResponseEntity<>("The sending account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(toAccountNumber.isBlank() || accountService.getAccountByNumber(toAccountNumber) == null){
            return new ResponseEntity<>("The receiving account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(Objects.equals(fromAccountNumber, toAccountNumber)){
            return new ResponseEntity<>("Sender and receiver accounts are the same", HttpStatus.FORBIDDEN);
        }
        Account sender = accountService.getAccountByNumber(fromAccountNumber);
        Account receiver = accountService.getAccountByNumber(toAccountNumber);
        if(client != sender.getClient()){
            return new ResponseEntity<>("You don't own the sending account", HttpStatus.FORBIDDEN);
        }
        if(amount.isNaN() || amount <= 0){
            return new ResponseEntity<>("Amount field is incomplete", HttpStatus.FORBIDDEN);
        }
        if(description.isBlank()){
            return  new ResponseEntity<>("Description field is empty",HttpStatus.FORBIDDEN);
        }
        if(sender.getBalance() < amount){
            return new ResponseEntity<>("Stated amount is greater than the account's balance", HttpStatus.FORBIDDEN);
        }
        Transaction transactionStart = transactionService.generateNewTransaction(TransactionType.DEBIT, amount, receiver.getNumber(), description);
        Transaction transactionEnd = transactionService.generateNewTransaction(TransactionType.CREDIT, amount, sender.getNumber(), description);
        sender.addTransactions(transactionStart);
        receiver.addTransactions(transactionEnd);
        transactionService.saveTransaction(transactionEnd);
        transactionService.saveTransaction(transactionStart);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
