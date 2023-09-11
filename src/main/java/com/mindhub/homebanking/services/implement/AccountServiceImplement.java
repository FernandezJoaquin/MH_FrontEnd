package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private ClientService clientService;

    @Override
    public List<AccountDTO> getAccounts(){
        return accountRepo.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @Override
    public AccountDTO getAccountDTOById(Long id){
        return new AccountDTO(accountRepo.findById(id).orElse(null));
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountRepo.findByNumber(accountNumber);
    }

    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        return  clientService.getClient(authentication.getName()).getAccounts().stream().map(AccountDTO::new).collect(toList());
    }
    @Override
    public String generateAccountNumber(){
        String accountNumber;
        do {
            accountNumber = "VIN"+random(0,99999999);
        }while(accountRepo.findByNumber(accountNumber) != null);
        return accountNumber;
    }
    @Override
    public Account generateNewAccount(){
        return new Account(this.generateAccountNumber(), LocalDate.now(),0);
    }
    @Override
    public void saveAccount(Account account){
        accountRepo.save(account);
    }
    public String random(int min, int max) {
        int rando= (int) ((Math.random() * (max - min)) + min);
        return Integer.toString(rando);
    }
}
