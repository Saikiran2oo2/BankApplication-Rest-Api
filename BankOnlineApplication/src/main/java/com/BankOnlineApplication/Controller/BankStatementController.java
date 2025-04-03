package com.BankOnlineApplication.Controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BankOnlineApplication.Entity.TransactionEntity;
import com.BankOnlineApplication.Service.BankStatementIMPL;
import com.itextpdf.text.DocumentException;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/bankStatement")
public class BankStatementController {
	@Autowired
	private BankStatementIMPL bankStatement;
	@GetMapping
	public List<TransactionEntity> generateBankStatement(@RequestParam String accountNumber,@RequestParam String startDate,@RequestParam String endDate) throws FileNotFoundException, DocumentException{
		
		return bankStatement.generateStatement(accountNumber, startDate, endDate);
	}
	

}
