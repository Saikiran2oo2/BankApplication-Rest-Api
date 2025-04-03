package com.BankOnlineApplication.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
	private String transactionType;
	private BigDecimal amount;
	private String accountNumber;
	private String status;


}
