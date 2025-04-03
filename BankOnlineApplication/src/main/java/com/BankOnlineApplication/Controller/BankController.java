package com.BankOnlineApplication.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BankOnlineApplication.DTO.BankResponse;
import com.BankOnlineApplication.DTO.CreditDebitRequest;
import com.BankOnlineApplication.DTO.EnqurieRequest;
import com.BankOnlineApplication.DTO.TransferRequest;
import com.BankOnlineApplication.DTO.UserRequest;
import com.BankOnlineApplication.Entity.UserDetails;
import com.BankOnlineApplication.Service.BankService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/Bank")
@Tag(name="User Bank Management System")
public class BankController {
	@Autowired
	BankService bankService;
	@Operation(summary = "Create a new account", description = "This endpoint allows users to create a new bank account.")
	@PostMapping("/CreateAccount")
	public BankResponse createAccount(@RequestBody UserRequest userRequest) {
		return bankService.createAccount(userRequest);
		
	}
	@Operation(summary = "Account Balance Check", description = "This endpoint allows users to check the Account balance.")
	@GetMapping("/BalanceEnqury")
	public BankResponse balanceEnqury(@RequestBody EnqurieRequest request) {
		return bankService.balanceEnquiry(request);
	}
	@Operation(summary = "Check Account Exixted or not", description = "This endpoint allows users to Check Account Exixted or not.")
	@GetMapping("/nameEnqury")
	public String nameEnqury(@RequestBody EnqurieRequest request) {
		return bankService.nameEnquire(request);
	}
	@Operation(summary = "credit amount to the account", description = "This endpoint allows users to credit amount to the account.")
	@PostMapping("/Deposit")
	public BankResponse creditAmount(@RequestBody CreditDebitRequest creditAmount) {
		return bankService.creditAmount(creditAmount);
	}
	@Operation(summary = "debiting amount from Account", description = "This endpoint allows users to debiting amount from Account.")
	@PostMapping("/debit")
	public BankResponse debitAmount(@RequestBody CreditDebitRequest debitAmount) {
		return bankService.debitAmount(debitAmount);
	}
	@Operation(summary = "Transfer Amount from one Account to Another Account", description = "This endpoint allows users to Transfer Amount from one Account to Another Account.")
	@PostMapping("/Transfer")
	public BankResponse accountTransfer(@RequestBody TransferRequest request) {
		return bankService.accountTransfer(request);
	}
	@Operation(summary = "All user Saved in the DataBase", description = "This endpoint allows users to check All user Saved in the DataBase.")
	@GetMapping("/allUsers")
	public List<UserDetails> allUsers() {
		List<UserDetails> allUser=bankService.allUsers();
		return allUser;
	}

}
