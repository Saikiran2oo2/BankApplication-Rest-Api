package com.BankOnlineApplication.Service;

import com.BankOnlineApplication.DTO.TransactionRequest;

public interface TransactionService {
	void saveTransaction(TransactionRequest transactionDto);

}
