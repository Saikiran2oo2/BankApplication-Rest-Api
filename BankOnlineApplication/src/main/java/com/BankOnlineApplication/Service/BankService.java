package com.BankOnlineApplication.Service;
import java.util.List;

import com.BankOnlineApplication.DTO.BankResponse;
import com.BankOnlineApplication.DTO.CreditDebitRequest;
import com.BankOnlineApplication.DTO.EnqurieRequest;
import com.BankOnlineApplication.DTO.TransferRequest;
import com.BankOnlineApplication.DTO.UserRequest;
import com.BankOnlineApplication.Entity.UserDetails;

public interface BankService {
	
	BankResponse createAccount(UserRequest userRequest);
	
	BankResponse balanceEnquiry(EnqurieRequest request);
	
	String nameEnquire(EnqurieRequest request);
	
	BankResponse creditAmount(CreditDebitRequest creditAmount);
	
	BankResponse debitAmount(CreditDebitRequest debitAmount);
	
	BankResponse accountTransfer(TransferRequest request);
	
	List<UserDetails> allUsers();

}
