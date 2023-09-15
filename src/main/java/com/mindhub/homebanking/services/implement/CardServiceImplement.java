package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mindhub.homebanking.utils.CardUtils.getCardNumber;
import static com.mindhub.homebanking.utils.CardUtils.getCvv;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepo;

    @Override
    public String generateNewNumber() {
        String cardNumber;
        do {
            cardNumber = CardUtils.getCardNumber();
        }while (cardRepo.findByNumber(cardNumber) != null);
        return cardNumber;
    }

    @Override
    public Card generateNewCard(CardType type, CardColor color) {
        return new Card(type, this.generateNewNumber() , CardUtils.getCvv(), color);
    }

    @Override
    public void saveCard(Card card) {
        cardRepo.save(card);
    }


}
