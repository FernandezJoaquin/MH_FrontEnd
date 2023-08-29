package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String cardholder;
    private CardType type;
    private String number;
    private String cvv;
    private CardColor color;
    private LocalDate thruDate;
    private LocalDate fromDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    @JsonIgnore
    private Client client;
    public Card(){}

    public Card(Client cardholder, CardType type, String number, String cvv, CardColor color) {
        this.client = cardholder;
        this.cardholder = cardholder.getFirstName()+cardholder.getLastName();
        this.type = type;
        this.number = number;
        this.cvv = cvv;
        this.color = color;
        this.fromDate = LocalDate.now();
        this.thruDate = LocalDate.now().plusYears(5);
    }

    public void setClient(Client client) {
        this.client = client;
        this.setCardholder();
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public void setCardholder(){
        this.cardholder = client.getFirstName()+client.getLastName();
    }

    public Client getClient(){
        return client;
    }

    public String getCardholder() {
        return cardholder;
    }

    public CardType getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
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

    public Long getId() {
        return id;
    }
}
