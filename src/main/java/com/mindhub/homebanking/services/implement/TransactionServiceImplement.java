package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    TransactionRepository transactionRepo;
    @Autowired
    AccountService accountService;
    
    @Override
    public Transaction generateNewTransaction(TransactionType type, double amount, String accountNumber,
                                              String description) {
        return new Transaction(type, amount, accountNumber+":"+description);
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepo.save(transaction);
    }

    @Override
    public Transaction generateNewTransactionLoan(TransactionType type, double amount, String accountNumber) {
        return new Transaction(type, amount, accountNumber+": "+"Loan approved");
    }

}
