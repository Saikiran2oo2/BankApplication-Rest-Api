package com.BankOnlineApplication.DTO;

import java.time.Year;

public class AccountNumberGenerator {
	public static String generateRandomAccountNumber() {
		// CURRENT YEAR (2025) + RANDOM 6 DIGITS = ACCOUNT NUMBER
		
		Year currentYear=Year.now();
		int min=100000;
		int max=999999;
		
		
		int randomNumber=(int) Math.floor(Math.random()*(max-min +1)+min);
		String year=String.valueOf(currentYear);
		String randNumber=String.valueOf(randomNumber);
		StringBuilder accountNumber=new StringBuilder();
		return accountNumber.append(year).append(randNumber).toString();
		
	}

}
