package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.UserRole;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    ClientRepository clientRepo;

    @Autowired
    AccountRepository accountRepo;
    private Optional<Client> optionalClient;
    public ClientController(){this.clientRepo = clientRepo;}

    public ClientRepository getClientRepo() {
        return clientRepo;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;


    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepo.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientRepo.findById(id).orElse(null));
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String firstName,
    @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepo.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password), UserRole.CLIENT);
        Account account = new Account("VIN-"+random(0,99999999), LocalDate.now(),0);
        clientRepo.save(client);
        client.addAccount(account);
        accountRepo.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping("/clients/current")
    public ClientDTO getCurrent(Authentication authentication){
        return new ClientDTO(clientRepo.findByEmail(authentication.getName()));
    }

    public String random(int min, int max) {
        int rando= (int) ((Math.random() * (max - min)) + min);
        return Integer.toString(rando);
    }

}
