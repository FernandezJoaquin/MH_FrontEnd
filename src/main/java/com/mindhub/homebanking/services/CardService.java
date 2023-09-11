package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

public interface CardService {
    String generateNewNumber();

    Card generateNewCard(CardType type, CardColor color);

    void saveCard(Card card);
}
