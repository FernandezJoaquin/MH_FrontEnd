package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    LoanRepository loanRepo;

    @Autowired
    ClientLoanRepository clientLoanRepo;

    @Override
    public List<LoanDTO> getLoans() {
        return loanRepo.findAll().stream().map(LoanDTO::new).collect(toList());
    }

    @Override
    public boolean loanCheck(Long id) {
        return loanRepo.findById(id).isEmpty();
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepo.findById(id).get();
    }

    @Override
    public ClientLoan generateNewClientLoan(double amount, Integer payments) {
        return new ClientLoan(amount*1.2, payments);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepo.save(clientLoan);
    }
}
