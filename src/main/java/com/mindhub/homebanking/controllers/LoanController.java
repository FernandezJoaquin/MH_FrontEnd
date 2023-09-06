package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    LoanRepository loanRepo;
    @Autowired
    AccountRepository accountRepo;
    @Autowired
    TransactionRepository transactionRepo;
    @Autowired
    ClientLoanRepository clientLoanRepo;

    public LoanController() {}
    public LoanRepository getLoanRepo(){
        return loanRepo;
    }
    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepo.findAll().stream().map(LoanDTO::new).collect(toList());
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
        if(loanRepo.findById(loan.getLoanId()).isEmpty()){
            return new ResponseEntity<>("The loan you are requesting doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(loan.getAmount() > loanRepo.findById(loan.getLoanId()).get().getMaxAmount() ){
            return new ResponseEntity<>("Requested amount is higher than allowed", HttpStatus.FORBIDDEN);
        }
        if(!loanRepo.findById(loan.getLoanId()).get().getPayments().contains(loan.getPayments())){
            return new ResponseEntity<>("Choose one of the allowed number of payments", HttpStatus.FORBIDDEN);
        }
        if(accountRepo.findByNumber(loan.getAccount()) == null){
            return new ResponseEntity<>("The account your want to credit doesn't exist ", HttpStatus.FORBIDDEN);
        }
        if(accountRepo.findByNumber(loan.getAccount()).getClient().getEmail() != authentication.getName()){
            return new ResponseEntity<>("You don't own this account", HttpStatus.FORBIDDEN);
        }
        Transaction transaction = new Transaction(TransactionType.CREDIT, loan.getAmount(),
                loanRepo.findById(loan.getLoanId()).get().getName()+":"+"Loan Aproved");
        ClientLoan clientLoan = new ClientLoan(loan.getAmount()*1.2,loan.getPayments());
        loanRepo.findById(loan.getLoanId()).get().addClientLoan(clientLoan);
        accountRepo.findByNumber(loan.getAccount()).getClient().addClientLoan(clientLoan);

        accountRepo.findByNumber(loan.getAccount()).addTransactions(transaction);
        transactionRepo.save(transaction);
        clientLoanRepo.save(clientLoan);

        return new ResponseEntity<>(HttpStatus.CREATED);


    }

}
