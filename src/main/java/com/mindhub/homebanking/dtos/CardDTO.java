package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private Long id;
    private String cardholder;
    private CardType type;
    private Integer number;
    private Integer cvv;
    private CardColor color;
    private LocalDate thruDate;
    private LocalDate fromDate;

    public CardDTO(){}

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardholder = card.getCardholder();
        this.type = card.getType();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.color = card.getColor();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
    }

    public Long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public CardType getType() {
        return type;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public CardColor getColor() {
        return color;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }
}
