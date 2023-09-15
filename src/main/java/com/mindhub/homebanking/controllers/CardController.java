package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    CardService cardService;

    @Autowired
    ClientService clientService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard(@RequestParam CardType cardType, @RequestParam CardColor cardColor,
                                          Authentication authentication) {
        if(clientService.getClient(authentication.getName()) == null){
            return new ResponseEntity<>("This client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (cardType == null) {
            return new ResponseEntity<>("Card type field is empty", HttpStatus.FORBIDDEN);
        }
        if (cardColor == null) {
            return new ResponseEntity<>("Card color field is empty", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.getClient(authentication.getName());
        Set<Card> creditCards = client.getCards().stream().filter(card -> card.getType() == CardType.CREDIT).collect(Collectors.toSet());
        Set<Card> debitCards = client.getCards().stream().filter(card -> card.getType() == CardType.DEBIT).collect(Collectors.toSet());
        if (cardType == CardType.CREDIT & creditCards.size() >= 3) {
                return new ResponseEntity<>("Max credit cards reached", HttpStatus.FORBIDDEN);
        }
        if (cardType == CardType.DEBIT && debitCards.size() >= 3) {
            return new ResponseEntity<>("Max debit cards reached", HttpStatus.FORBIDDEN);
        }
        Card card = cardService.generateNewCard(cardType, cardColor);
        client.addCard(card);
        cardService.saveCard(card);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}


