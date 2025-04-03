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
public class UserRequest {
	private Long id;
	private String firstName;
	private String lastName;
	private String otherName;
	private String gender;
	private String address;
	private String state;
	private String city;
	private String email;
	private String mobileNumber;
	private String alternativeMobileNumber;
	private BigDecimal accountBalanace;

}
