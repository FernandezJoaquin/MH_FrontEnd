package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    CardRepository cardRepo;

    @Autowired
    ClientRepository clientRepo;

    public CardController() {
        this.cardRepo = cardRepo;
    }

    public CardRepository getCardRepo() {
        return cardRepo;
    }

    public List<CardDTO> getClients() {
        return cardRepo.findAll().stream().map(CardDTO::new).collect(toList());
    }

    @RequestMapping("/cards/{id}")
    public CardDTO getCards(@PathVariable Long id) {
        return new CardDTO(cardRepo.findById(id).orElse(null));
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> addCard(@RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication) {
        Client client = clientRepo.findByEmail(authentication.getName());
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Max cards reached", HttpStatus.FORBIDDEN);
        }
        Card card = new Card(client, cardType, random(1000,9999)+"-"+random(1000,9999)+"-"+random(1000,9999)+"-"+random(1000,9999),
                random(100,999), cardColor);
        cardRepo.save(card);
        client.addCard(card);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
    public String random(int min, int max) {
             int rando= (int) ((Math.random() * (max - min)) + min);
             return Integer.toString(rando);
    }

}

