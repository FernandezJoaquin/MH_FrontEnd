package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<LoanDTO> getLoans();

    boolean loanCheck(Long id);

    Loan getLoanById(Long id);

    ClientLoan generateNewClientLoan(double amount, Integer payments);

    void saveClientLoan(ClientLoan clientLoan);
}
