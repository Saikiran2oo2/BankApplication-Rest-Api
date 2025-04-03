package com.BankOnlineApplication.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
	
	private String accountNumber;
	private BigDecimal accountBalance;
	private String accountName;

}
