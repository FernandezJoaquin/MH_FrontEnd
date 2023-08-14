package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository cRepository, AccountRepository aRepository,
									  TransactionRepository tRepository, LoanRepository lRepository, ClientLoanRepository clRepo) {
		return (args) -> {
			// save a couple of customers
			Client cl = new Client("Melba", "Morel", "melba@mindhub.com");
			Account ac =new Account("VIN001", LocalDate.now(),5000);
			Loan hipo = new Loan("Hipotecario", 500000, new ArrayList<>(List.of(12,24,36,48,60)));
			Loan personal = new Loan("Personal", 100000, new ArrayList<>(List.of(6,12,24)));
			Loan auto = new Loan("Automotriz", 300000, new ArrayList<>(List.of(6,12,24,36)));
			ClientLoan clientLoan = new ClientLoan(400000.0,60,cl,hipo);
			cRepository.save(cl);
			lRepository.save(hipo);
			lRepository.save(personal);
			lRepository.save(auto);
			clRepo.save(clientLoan);
			clientLoan = new ClientLoan(50000.0,12,cl,personal);
			clRepo.save(clientLoan);
			cl.addAccount(ac);
			aRepository.save(ac);
			Transaction tr = new Transaction(TransactionType.DEBITO, 658,"algo");
			ac.addTransactions(tr);
			tRepository.save(tr);
			ac = new Account("VIN002", LocalDate.now().plusDays(1),7500);
			cl.addAccount(ac);
			aRepository.save(ac);
			cl = new Client("Zelba", "Morel", "Zelba@mindhub.com");
			cRepository.save(cl);
			clientLoan = new ClientLoan(100000.0,24,cl,personal);
			clRepo.save(clientLoan);
			clientLoan = new ClientLoan(200000.0,36,cl,auto);
			clRepo.save(clientLoan);
			ac = new Account("VIN003",LocalDate.now(),800);
			cl.addAccount(ac);
			aRepository.save(ac);

		};
	}
}
