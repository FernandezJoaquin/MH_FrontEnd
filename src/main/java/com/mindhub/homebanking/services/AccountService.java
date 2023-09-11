package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccounts();
    AccountDTO getAccountDTOById(Long id);

    Account getAccountByNumber(String accountNumber);

    List<AccountDTO> getCurrentAccounts(Authentication authentication);

    String generateAccountNumber();

    Account generateNewAccount();

    void saveAccount(Account account);

}
