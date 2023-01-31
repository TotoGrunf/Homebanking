package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
//			Client client1 = new Client("Melba", "Morel","melba@mindhub.com",passwordEncoder.encode("melbita"));
//			Client client2 = new Client("Guille", "Giovanelli","guillerminag2001@hotmail.com",passwordEncoder.encode("cacho"));
//			Client admin1 = new Client("tomi","grunfeld","tomigrunfeld@admin.com",passwordEncoder.encode("admin"));
//			Account account1 = new Account("VIN001", 5000.0, LocalDateTime.now(),client1);
//			Account account2 = new Account("VIN002", 7500.0, LocalDateTime.now().plusDays(1));
//			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 5000.0, "Credit transaction", LocalDateTime.now().plusHours(3), account1);
//			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -2500.0, "Debit transaction", LocalDateTime.now().plusDays(20).plusHours(7).plusMinutes(29), account1);
//			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 2500.0, "Credit transaction", LocalDateTime.now().plusDays(40).plusHours(13).plusMinutes(43), account1);
//			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 15000.0, "Credit transaction", LocalDateTime.now(), account2);
//			Transaction transaction5 = new Transaction(TransactionType.DEBIT, -5537.0, "Credit transaction", LocalDateTime.now(), account2);
//			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -1963.0, "Credit transaction", LocalDateTime.now(), account2);
//			Loan Hipotecario = new Loan("Hipotecario" , 500000.00, List.of(12,24,36,48,60));
//			Loan Personal = new Loan("Personal" , 100000.00, List.of(6,12,24));
//			Loan Automotriz = new Loan("Automotriz" , 300000.00, List.of(6,12,24,36));
//			ClientLoan loan1 = new ClientLoan(400000.00,60,client1,Hipotecario);
//			ClientLoan loan2 = new ClientLoan(50000.00,12,client1,Personal);
//			ClientLoan loan3 = new ClientLoan(100000.00,24,client2,Personal);
//			ClientLoan loan4 = new ClientLoan(200000.00,36,client2,Automotriz);
//			Card card1 = new Card(client1,(client1.getFirstName() + " " + client1.getLastName()),CardType.DEBIT, CardColor.GOLD,"5030-3695-5876-6231",982,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
//			Card card2 = new Card(client1,(client1.getFirstName() + " " + client1.getLastName()),CardType.CREDIT, CardColor.TITANIUM,"3103-5785-7892-4537",582,LocalDateTime.now(),LocalDateTime.now().plusYears(7));
//			Card card3 = new Card(client2,(client2.getFirstName() + " " + client2.getLastName()),CardType.DEBIT, CardColor.SILVER,"4566-7869-4532-2305",369,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
//
//			client1.addAccount(account2);
//
//			clientRepository.save(client1);
//			clientRepository.save(client2);
//			clientRepository.save(admin1);
//			accountRepository.save(account1);
//			accountRepository.save(account2);
//			transactionRepository.save(transaction1);
//			transactionRepository.save(transaction2);
//			transactionRepository.save(transaction3);
//			transactionRepository.save(transaction4);
//			transactionRepository.save(transaction5);
//			transactionRepository.save(transaction6);
//			loanRepository.save(Hipotecario);
//			loanRepository.save(Personal);
//			loanRepository.save(Automotriz);
//			clientLoanRepository.save(loan1);
//			clientLoanRepository.save(loan2);
//			clientLoanRepository.save(loan3);
//			clientLoanRepository.save(loan4);
//			cardRepository.save(card1);
//			cardRepository.save(card2);
//			cardRepository.save(card3);

		};
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
}
