package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client() { }
    public Client(String firstName, String lastName, String email, String password, UserRole role){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public List<Loan> getLoans() {
        return clientLoans.stream().map(ClientLoan::getLoan).collect(toList());
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void addAccount(Account a) {
        a.setClient(this);
        this.accounts.add(a);
    }
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public void addCard(Card c) {
        c.setClient(this);
        this.cards.add(c);
    }
}
