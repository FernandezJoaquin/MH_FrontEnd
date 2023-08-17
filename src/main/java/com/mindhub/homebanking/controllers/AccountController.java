package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

    public AccountController() {
        this.accountRepo = accountRepo;
    }

    public AccountRepository getAccountRepo() {
        return accountRepo;
    }

    @RequestMapping("/accounts")
    public List<AccountDTO> getClients(){
        return accountRepo.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountRepo.findById(id).orElse(null));
    }
}
