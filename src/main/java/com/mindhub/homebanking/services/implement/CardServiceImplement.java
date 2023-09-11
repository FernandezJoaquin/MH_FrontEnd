package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepo;

    @Override
    public String generateNewNumber() {
        String cardNumber;
        do {
            cardNumber = random(0, 9999) + "-" + random(0, 9999) + "-" + random(0, 9999) + "-" + random(0, 9999);
        }while (cardRepo.findByNumber(cardNumber) != null);
        return cardNumber;
    }

    @Override
    public Card generateNewCard(CardType type, CardColor color) {
        return new Card(type, this.generateNewNumber() , random(0, 999), color);
    }

    @Override
    public void saveCard(Card card) {
        cardRepo.save(card);
    }

    public String random(int min, int max) {
        int rando = (int) ((Math.random() * (max - min)) + min);
        return Integer.toString(rando);
    }
}
