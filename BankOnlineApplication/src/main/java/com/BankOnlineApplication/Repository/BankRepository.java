package com.BankOnlineApplication.Repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.BankOnlineApplication.Entity.UserDetails;
import java.util.List;


public interface BankRepository extends JpaRepository<UserDetails, Long> {
	
	boolean existsByEmail(String email);
	
	boolean existsByAccountNumber(String accountNumber);
	
	
	UserDetails findByAccountNumber(String accountNumber);

}
