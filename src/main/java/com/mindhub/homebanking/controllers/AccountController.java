package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;


    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountDTOById(id);
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.GET)
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        return  accountService.getCurrentAccounts(authentication);
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> addAccount(Authentication authentication){
        if(clientService.getClient(authentication.getName()) == null){
            return new ResponseEntity<>("This client doesn't exist",HttpStatus.FORBIDDEN);
        }
        Client client = clientService.getClient(authentication.getName());
        if(client.getAccounts().size() >= 3){
            return new ResponseEntity<>("Max accounts reached",HttpStatus.FORBIDDEN);
        }
        Account account = accountService.generateNewAccount();
        client.addAccount(account);
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

