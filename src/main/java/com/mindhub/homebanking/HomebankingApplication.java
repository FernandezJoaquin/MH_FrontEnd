package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository cRepository, AccountRepository aRepository,TransactionRepository tRepository ) {
		return (args) -> {
			// save a couple of customers
			Client cl = new Client("Melba", "Morel", "melba@mindhub.com");
			Account ac =new Account("VIN001", LocalDate.now(),5000);
			cRepository.save(cl);
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
			ac = new Account("VIN003",LocalDate.now(),800);
			cl.addAccount(ac);
			aRepository.save(ac);

		};
	}
}
