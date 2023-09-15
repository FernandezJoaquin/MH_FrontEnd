package com.mindhub.homebanking;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void clientAccountCheck(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, everyItem(hasProperty("accounts", hasSize(greaterThanOrEqualTo(1)))));
    }

    @Test
    public void clientPasswordCheck(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, everyItem(hasProperty("password", isA(String.class))));
    }

    @Test
    public void properAccountNumber(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, not(hasItem(hasProperty("number", not(startsWith("VIN"))))));
    }

    @Test
    public void accountBalance(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, everyItem(hasProperty("balance",greaterThanOrEqualTo(0.0))));
    }

    @Test
    public void transactionDescription (){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem((hasProperty("description",is(not(empty()))))));
    }
    @Test
    public void transactionType (){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(both(hasProperty("type",is(TransactionType.CREDIT)))
                .and(hasProperty("amount", not(lessThanOrEqualTo(0.0))))));
    }
   @Test
    public void cardColorCheck(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, everyItem(anyOf(hasProperty("color",is(CardColor.SILVER)),
                hasProperty("color",is(CardColor.GOLD)),
                hasProperty("color",is(CardColor.TITANIUM)))));
    }
    @Test
    public void cardTypeCheck(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, not(hasItem(both((hasProperty("type",is(not(CardType.CREDIT)))))
                .and(hasProperty("type",is(not(CardType.DEBIT)))))));
    }
}
