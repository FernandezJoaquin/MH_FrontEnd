package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers
			Client admin = new Client("Admin","01","admin01@mindhub.com",
					passwordEncoder.encode("neuring"),UserRole.ADMIN);
			clientRepository.save(admin);
			Client cl = new Client("Melba", "Morel", "melba@mindhub.com",
					passwordEncoder.encode("lala"), UserRole.CLIENT);
			Account ac =new Account("VIN001", LocalDate.now(),5000);
			Loan hipo = new Loan("Hipotecario", 500000, new ArrayList<>(List.of(12,24,36,48,60)));
			Loan personal = new Loan("Personal", 100000, new ArrayList<>(List.of(6,12,24)));
			Loan auto = new Loan("Automotriz", 300000, new ArrayList<>(List.of(6,12,24,36)));
			ClientLoan clientLoan = new ClientLoan(400000.0,60,cl,hipo);
			Card melbaCardG = new Card(cl,CardType.DEBIT, "2314564", "547",CardColor.GOLD);
			Card melbaCardT = new Card(cl,CardType.DEBIT, "2142564", "547",CardColor.TITANIUM);
			clientRepository.save(cl);
			loanRepository.save(hipo);
			loanRepository.save(personal);
			loanRepository.save(auto);
			clientLoanRepository.save(clientLoan);
			cl.addCard(melbaCardT);
			cl.addCard(melbaCardG);
			clientLoan = new ClientLoan(50000.0,12,cl,personal);
			clientLoanRepository.save(clientLoan);
			cl.addAccount(ac);
			accountRepository.save(ac);
			Transaction tr = new Transaction(TransactionType.DEBITO, 658,"algo");
			ac.addTransactions(tr);
			transactionRepository.save(tr);
			ac = new Account("VIN002", LocalDate.now().plusDays(1),7500);
			cl.addAccount(ac);
			accountRepository.save(ac);
			cl = new Client("Zelba", "Morel", "zelba@mindhub.com",
					passwordEncoder.encode("lala"),UserRole.CLIENT);
			clientRepository.save(cl);
			clientLoan = new ClientLoan(100000.0,24,cl,personal);
			clientLoanRepository.save(clientLoan);
			clientLoan = new ClientLoan(200000.0,36,cl,auto);
			clientLoanRepository.save(clientLoan);
			ac = new Account("VIN003",LocalDate.now(),800);
			cl.addAccount(ac);
			accountRepository.save(ac);
			Card zelbaCardS = new Card(cl,CardType.DEBIT, "9714564", "987",CardColor.SILVER);
			cardRepository.save(melbaCardG);
			cardRepository.save(melbaCardT);
			cardRepository.save(zelbaCardS);
			cl.addCard(zelbaCardS);

		};
	}
}
