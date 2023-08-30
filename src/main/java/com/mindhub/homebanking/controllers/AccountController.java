package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ClientRepository clientRepo;

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

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.GET)
    public List<AccountDTO> getAccounts(Authentication authentication){
        return  clientRepo.findByEmail(authentication.getName()).getAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> addAccount(Authentication authentication){
        Client client =clientRepo.findByEmail(authentication.getName());
        if(client.getAccounts().size() >= 3){
            return new ResponseEntity<>("Max accounts reached",HttpStatus.FORBIDDEN);
        }
        Account account = new Account("VIN"+random(0,99999999), LocalDate.now(),0);
        client.addAccount(account);
        accountRepo.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public String random(int min, int max) {
        int rando= (int) ((Math.random() * (max - min)) + min);
        return Integer.toString(rando);
    }
}

