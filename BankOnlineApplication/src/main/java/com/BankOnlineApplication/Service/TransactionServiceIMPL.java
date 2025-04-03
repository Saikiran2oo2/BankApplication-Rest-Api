package com.BankOnlineApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.BankOnlineApplication.DTO.TransactionRequest;
import com.BankOnlineApplication.Entity.TransactionEntity;
import com.BankOnlineApplication.Repository.TransactionRepository;
@Component
public class TransactionServiceIMPL implements TransactionService {
	@Autowired
	private TransactionRepository transactionRepo;

	@Override
	public void saveTransaction(TransactionRequest transactionDto) {
		TransactionEntity transaction=TransactionEntity.builder()
				.transactionType(transactionDto.getTransactionType())
				.accountNumber(transactionDto.getAccountNumber())
				.amount(transactionDto.getAmount())
				.status("SUCCESS")
				.build();
		transactionRepo.save(transaction);
		System.out.println("Transaction is saved.....");
		
	}

}
