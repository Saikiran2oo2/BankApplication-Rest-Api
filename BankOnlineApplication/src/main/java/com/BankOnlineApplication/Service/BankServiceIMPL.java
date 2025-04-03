package com.BankOnlineApplication.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BankOnlineApplication.DTO.AccountInfo;
import com.BankOnlineApplication.DTO.AccountNumberGenerator;
import com.BankOnlineApplication.DTO.AccountUtills;
import com.BankOnlineApplication.DTO.BankResponse;
import com.BankOnlineApplication.DTO.CreditDebitRequest;
import com.BankOnlineApplication.DTO.EmailDetails;
import com.BankOnlineApplication.DTO.EnqurieRequest;
import com.BankOnlineApplication.DTO.TransactionRequest;
import com.BankOnlineApplication.DTO.TransferRequest;
import com.BankOnlineApplication.DTO.UserRequest;
import com.BankOnlineApplication.Entity.UserDetails;
import com.BankOnlineApplication.Repository.BankRepository;

@Service
public class BankServiceIMPL implements BankService {

	@Autowired
	BankRepository bankRepo;
	@Autowired
	EmailService emailService;
	@Autowired
	TransactionService transactionService;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		// CREATE new USER

		if (bankRepo.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder().responseCode(AccountUtills.ACCOUNT_EXIST_CODE)
					.responseMessage(AccountUtills.ACCOUNT_EXIST_MESSAGE).accountInfo(null).build();
		}
		UserDetails newUser = UserDetails.builder().firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName()).otherName(userRequest.getOtherName())
				.gender(userRequest.getGender()).address(userRequest.getAddress()).state(userRequest.getState())
				.city(userRequest.getCity()).accountNumber(AccountNumberGenerator.generateRandomAccountNumber())
				.accountBalanace(BigDecimal.ZERO).email(userRequest.getEmail())
				.mobileNumber(userRequest.getMobileNumber())
				.alternativeMobileNumber(userRequest.getAlternativeMobileNumber()).status("JUST CREATED").build();

		UserDetails user = bankRepo.save(newUser);

		// SENDING EMAIL

		EmailDetails emailDetails = EmailDetails.builder().recipient(user.getEmail())
				.subject("$$$$$--> ACCOUNT CREATION <--$$$$$")
				.messageBody("CONGRATULATIONS ACCOUNT HAS BEEN CREATED SUCCESSFULLY!\n" + "ACCOUNT NAME : "
						+ user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName() + "\n"
						+ " ACCOUNT NUMBER :" + user.getAccountNumber())
				.build();
		emailService.sendMail(emailDetails);

		return BankResponse.builder().responseCode(AccountUtills.ACCOUNT_CREATED_CODE)
				.responseMessage(AccountUtills.ACCOUNT_CREATED_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
						.accountNumber(user.getAccountNumber()).accountBalance(user.getAccountBalanace()).build())
				.build();

	}

	@Override
	public BankResponse balanceEnquiry(EnqurieRequest request) {
		// checks account number is existed or not then perform action of balance
		// enqurie
		boolean isAccountExist = bankRepo.existsByAccountNumber(request.getAccountNumber());

		if (!isAccountExist) {
			return BankResponse.builder().responseCode(AccountUtills.ACCOUNTNUMBER_NOT_EXISTS_CODE)
					.responseMessage(AccountUtills.ACCOUNTNUMBER_NOT_EXISTS_MESSAGE).accountInfo(null).build();
		}

		UserDetails foundUser = bankRepo.findByAccountNumber(request.getAccountNumber());
		return BankResponse.builder().responseCode(AccountUtills.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtills.ACCOUNT_FOUND_MESSAGE)
				.accountInfo(AccountInfo.builder().accountNumber(foundUser.getAccountNumber())
						.accountBalance(foundUser.getAccountBalanace())
						.accountName(foundUser.getFirstName() + " " + foundUser.getLastName()).build())
				.build();
	}

	@Override
	public String nameEnquire(EnqurieRequest request) {
		boolean isAccountExist = bankRepo.existsByAccountNumber(request.getAccountNumber());

		if (!isAccountExist) {
			return AccountUtills.ACCOUNTNUMBER_NOT_EXISTS_MESSAGE;
		}

		UserDetails foundUser = bankRepo.findByAccountNumber(request.getAccountNumber());
		return foundUser.getFirstName();
	}

	
	
	@Override
	public BankResponse creditAmount(CreditDebitRequest creditAmount) {
		boolean isAccountExist = bankRepo.existsByAccountNumber(creditAmount.getAccountNumber());

		if (!isAccountExist) {
			return BankResponse.builder().responseCode(AccountUtills.ACCOUNTNUMBER_NOT_EXISTS_CODE)
					.responseMessage(AccountUtills.ACCOUNTNUMBER_NOT_EXISTS_MESSAGE).accountInfo(null).build();
		}

		UserDetails foundUser = bankRepo.findByAccountNumber(creditAmount.getAccountNumber());
		foundUser.setAccountBalanace(foundUser.getAccountBalanace().add(creditAmount.getAmount()));
		bankRepo.save(foundUser);
		
		//save transaction
		
		TransactionRequest transactionDto=TransactionRequest.builder()
				.accountNumber(foundUser.getAccountNumber())
				.transactionType("CREDIT")
				.amount(creditAmount.getAmount())
				.build();
		transactionService.saveTransaction(transactionDto);
		
		return BankResponse.builder().responseCode(AccountUtills.AMOUNT_CREDITED_SUCCESS_CODE)
				.responseMessage(AccountUtills.AMOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder().accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
						.accountNumber(foundUser.getAccountNumber()).accountBalance(foundUser.getAccountBalanace())
						.build())
				.build();
	}

	@Override
	public BankResponse debitAmount(CreditDebitRequest debitAmount) {
		boolean isAccountExist = bankRepo.existsByAccountNumber(debitAmount.getAccountNumber());

		if (!isAccountExist) {
			return BankResponse.builder().responseCode(AccountUtills.ACCOUNTNUMBER_NOT_EXISTS_CODE)
					.responseMessage(AccountUtills.ACCOUNTNUMBER_NOT_EXISTS_MESSAGE).accountInfo(null).build();
		}
		UserDetails foundUser = bankRepo.findByAccountNumber(debitAmount.getAccountNumber());

		// Check if the account balance is sufficient
		if (foundUser.getAccountBalanace().compareTo(debitAmount.getAmount()) < 0) {
			// If debit amount is greater than the current balance
			return BankResponse.builder().responseCode(AccountUtills.INSUFFICIENT_FUNDS_CODE) // Create a code like
																								// "4003"
					.responseMessage(AccountUtills.INSUFFICIENT_FUNDS_MESSAGE) // Message: "Insufficient funds"
					.accountInfo(AccountInfo.builder()
							.accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
							.accountNumber(foundUser.getAccountNumber()).accountBalance(foundUser.getAccountBalanace()) // Show
																														// current
																														// balance
							.build())
					.build();
		}
		foundUser.setAccountBalanace(foundUser.getAccountBalanace().subtract(debitAmount.getAmount()));
		bankRepo.save(foundUser);
		//save transaction
		
				TransactionRequest transactionDto=TransactionRequest.builder()
						.accountNumber(foundUser.getAccountNumber())
						.transactionType("DEBIT")
						.amount(debitAmount.getAmount())
						.build();
				transactionService.saveTransaction(transactionDto);
		
		return BankResponse.builder().responseCode(AccountUtills.AMOUNT_DEBITED_SUCCESS_CODE)
				.responseMessage(AccountUtills.AMOUNT_DEBITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder().accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
						.accountNumber(foundUser.getAccountNumber()).accountBalance(foundUser.getAccountBalanace())
						.build())
				.build();
	}

	@Override
	public BankResponse accountTransfer(TransferRequest request) {
		boolean isSourceAccountExist = bankRepo.existsByAccountNumber(request.getSourceAccontNumber());
		boolean isDestinationAccontIsExist = bankRepo.existsByAccountNumber(request.getDestinationAccountNumber());

		// checking sourceAccount number
		if (!isSourceAccountExist) {
			return BankResponse.builder().responseCode(AccountUtills.SOURCE_ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtills.SOURCE_ACCOUNT_NOT_EXISTS_MESSAGE).accountInfo(null).build();
		}
		// check if destination account is exist or not
		if (!isDestinationAccontIsExist) {
			return BankResponse.builder().responseCode(AccountUtills.DESTINATION_ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtills.DESTINATION_ACCOUNT_NOT_EXISTS_MESSAGE).accountInfo(null).build();
		}
		UserDetails sourceAccount = bankRepo.findByAccountNumber(request.getSourceAccontNumber());
		UserDetails destinationAccount = bankRepo.findByAccountNumber(request.getDestinationAccountNumber());
		// checks debiting amount is not greter then the amount in the source account
		if (sourceAccount.getAccountBalanace().compareTo(request.getAmount()) < 0) {
			return BankResponse.builder().responseCode(AccountUtills.INSUFFICIENT_FUNDS_CODE)
					.responseMessage(AccountUtills.INSUFFICIENT_FUNDS_MESSAGE).accountInfo(null).build();
		}
		// subtracting amount from source accont
		sourceAccount.setAccountBalanace(sourceAccount.getAccountBalanace().subtract(request.getAmount()));
		// crediting amount from souce accont to destination account
		destinationAccount.setAccountBalanace(destinationAccount.getAccountBalanace().add(request.getAmount()));
		// sending email alert for debiting amount
		EmailDetails debitAlert = EmailDetails.builder().subject("$$--> DEBIT ALERT <--$$")
				.recipient(sourceAccount.getEmail())
				.messageBody("The Amount of " + request.getAmount()
						+ "/- Rupees has been debited from your account! Your Current Acconut Balance "
						+ sourceAccount.getAccountBalanace() + "/-")
				.build();
		emailService.sendMail(debitAlert);
		bankRepo.save(sourceAccount);
		// sending email alert for crediting amount
		EmailDetails creditAlert = EmailDetails.builder().subject("$$--> DEBIT ALERT <--$$")
				.recipient(destinationAccount.getEmail())
				.messageBody("The Amount of " + request.getAmount()
						+ "/- Rupees has been credited to your account! from " + sourceAccount.getAccountNumber()
						+ " Your Current Acconut Balance " + destinationAccount.getAccountBalanace() + "/-")
				.build();
		emailService.sendMail(creditAlert);
		bankRepo.save(destinationAccount);
		
		//save transaction
		
				TransactionRequest transactionDto=TransactionRequest.builder()
						.accountNumber(destinationAccount.getAccountNumber())
						.transactionType("CREDIT")
						.amount(request.getAmount())
						.build();
				transactionService.saveTransaction(transactionDto);

		return BankResponse.builder().responseCode(AccountUtills.TRANSFER_SUCCESS_CODE)
				.responseMessage(AccountUtills.TRANSFER_SUCCESS_MESSAGE).accountInfo(null).build();
	}

	@Override
	public List<UserDetails> allUsers() {
		List<UserDetails> allUsers = bankRepo.findAll();
		return allUsers;
	}

}
