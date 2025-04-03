package com.BankOnlineApplication.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TransactionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String transactionID;
	private String transactionType;
	private BigDecimal amount;
	private String accountNumber;
	private String status;
	@CreationTimestamp
	private LocalDate createdAt;
	@UpdateTimestamp
	private LocalDate modifiedAt;

}
