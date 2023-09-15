package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    LoanService loanService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    ClientService clientService;

    public LoanController() {}

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoans();
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> addLoan(@RequestBody LoanApplicationDTO loan, Authentication authentication){
        if(clientService.getClient(authentication.getName()) == null){
            return new ResponseEntity<>("You aren't a registered client", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.getClient(authentication.getName());
        if(loan.getLoanId() == null || loanService.loanCheck(loan.getLoanId())){
            return new ResponseEntity<>("Input a valid loan type", HttpStatus.FORBIDDEN);
        }
        Loan verifiedLoan = loanService.getLoanById(loan.getLoanId());
        if(loan.getAccount().isBlank() || accountService.getAccountByNumber(loan.getAccount()) == null){
            return new ResponseEntity<>("Input an existing account", HttpStatus.FORBIDDEN);
        }
        Account verifiedAccount = accountService.getAccountByNumber(loan.getAccount());
        if(!Objects.equals(client.getEmail(), authentication.getName())){
            return new ResponseEntity<>("You don't own this account", HttpStatus.FORBIDDEN);
        }
        if(!verifiedLoan.getPayments().contains(loan.getPayments())){
            return new ResponseEntity<>("Choose one of the allowed number of payments", HttpStatus.FORBIDDEN);
        }
        if(loan.getAmount() <= 0 || loan.getAmount() > verifiedLoan.getMaxAmount() ){
            return new ResponseEntity<>("State an amount higher than 0", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = transactionService.generateNewTransactionLoan(TransactionType.CREDIT, loan.getAmount(),
                verifiedLoan.getName());
        ClientLoan clientLoan = loanService.generateNewClientLoan(loan.getAmount(),loan.getPayments());
        verifiedLoan.addClientLoan(clientLoan);
        client.addClientLoan(clientLoan);
        verifiedAccount.addTransactions(transaction);
        transactionService.saveTransaction(transaction);
        loanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>(HttpStatus.CREATED);


    }

}
