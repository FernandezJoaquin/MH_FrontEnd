package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    LoanService loanService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    public LoanController() {}

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoans();
    }
    @Transactional
    @RequestMapping(path="/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> addLoan(@RequestBody LoanApplicationDTO loan, Authentication authentication){
        if(authentication == null){
            return new ResponseEntity<>("You lack the credentials to perform this action", HttpStatus.FORBIDDEN);
        }
        if(loan.getLoanId() == null){
            return new ResponseEntity<>("Input a valid loan type", HttpStatus.FORBIDDEN);
        }
        if(loan.getPayments() <= 0){
            return new ResponseEntity<>("Choose one of the allowed number of payments", HttpStatus.FORBIDDEN);
        }
        if(loan.getAmount() <= 0){
            return new ResponseEntity<>("State an amount higher than 0", HttpStatus.FORBIDDEN);
        }
        if(loanService.loanCheck(loan.getLoanId())){
            return new ResponseEntity<>("The loan you are requesting doesn't exist", HttpStatus.FORBIDDEN);
        }
        Loan verifiedLoan = loanService.getLoanById(loan.getLoanId());
        if(loan.getAmount() > verifiedLoan.getMaxAmount() ){
            return new ResponseEntity<>("Requested amount is higher than allowed", HttpStatus.FORBIDDEN);
        }
        if(!verifiedLoan.getPayments().contains(loan.getPayments())){
            return new ResponseEntity<>("Choose one of the allowed number of payments", HttpStatus.FORBIDDEN);
        }
        if(accountService.getAccountByNumber(loan.getAccount()) == null){
            return new ResponseEntity<>("The account your want to credit doesn't exist ", HttpStatus.FORBIDDEN);
        }
        Account verifiedAccount = accountService.getAccountByNumber(loan.getAccount());
        if(verifiedAccount.getClient().getEmail() != authentication.getName()){
            return new ResponseEntity<>("You don't own this account", HttpStatus.FORBIDDEN);
        }
        Transaction transaction = transactionService.generateNewTransactionLoan(TransactionType.CREDIT, loan.getAmount(),
                verifiedLoan.getName());
        ClientLoan clientLoan = loanService.generateNewClientLoan(loan.getAmount(),loan.getPayments());
        verifiedLoan.addClientLoan(clientLoan);
        verifiedAccount.getClient().addClientLoan(clientLoan);

        verifiedAccount.addTransactions(transaction);
        transactionService.saveTransaction(transaction);
        loanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>(HttpStatus.CREATED);


    }

}
