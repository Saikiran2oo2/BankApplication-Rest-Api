package com.BankOnlineApplication.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BankOnlineApplication.Entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

}
