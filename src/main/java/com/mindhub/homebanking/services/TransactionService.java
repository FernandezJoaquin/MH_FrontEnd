package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

public interface TransactionService {
    Transaction generateNewTransaction(TransactionType type, double amount, String account,
                                       String description);
    void saveTransaction(Transaction transaction);

    Transaction generateNewTransactionLoan(TransactionType type, double amount, String accountNumber);
}
