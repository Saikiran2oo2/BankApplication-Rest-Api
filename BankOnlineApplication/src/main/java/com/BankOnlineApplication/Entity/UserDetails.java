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
public class UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String firstName;
	private String lastName;
	private String otherName;
	private String gender;
	private String address;
	private String state;
	private String city;
	private String accountNumber;
	private BigDecimal accountBalanace;
	private String email;
	private String mobileNumber;
	private String alternativeMobileNumber;
	private String status;
	@CreationTimestamp
	private LocalDate createdAt;
	@UpdateTimestamp
	private LocalDate modifiedAt;

}
