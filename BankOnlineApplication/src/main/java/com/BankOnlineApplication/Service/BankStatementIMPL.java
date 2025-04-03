package com.BankOnlineApplication.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.BankOnlineApplication.DTO.EmailDetails;
import com.BankOnlineApplication.Entity.TransactionEntity;
import com.BankOnlineApplication.Entity.UserDetails;
import com.BankOnlineApplication.Repository.BankRepository;
import com.BankOnlineApplication.Repository.TransactionRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatementIMPL {
	
	private TransactionRepository transactionRepo;
	@Autowired
	private BankRepository userRepo;
	private EmailService emailService;
	private static final String FILE="C:\\Users\\saiki\\OneDrive\\Documents\\MyStatement.pdf";
	
	/* retrive the Transactions List from a range of Date
	 * Generate PDF file of Transaction List
	 * send via email
	 * */
	
	public List<TransactionEntity> generateStatement(String accountNumber,String startDate,String endDate) throws FileNotFoundException, DocumentException{
		
		LocalDate start=LocalDate.parse(startDate,DateTimeFormatter.ISO_DATE);
		LocalDate end=LocalDate.parse(endDate,DateTimeFormatter.ISO_DATE);
		
		List<TransactionEntity> transactinList=transactionRepo.findAll().stream().filter(transaction ->transaction.getAccountNumber().equals(accountNumber))
				.filter(transaction ->transaction.getCreatedAt().isEqual(start)).filter(transaction->transaction.getCreatedAt().isEqual(end)).toList();
		
		
		//saving PDF of statement
		UserDetails user= userRepo.findByAccountNumber(accountNumber);
		Rectangle statementSize=new Rectangle(PageSize.A4);
		Document document=new Document(statementSize);
		log.info("setting size of document");
		java.io.OutputStream outputStream=new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputStream);
		document.open();
		
		PdfPTable bankInfoTable=new PdfPTable(1);
		PdfPCell bankName= new PdfPCell(new Phrase("KIRAN INTERNATIONAL BANK"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.BLUE);
		bankName.setPadding(20f);
		
		PdfPCell bankAdress=new PdfPCell(new Phrase("Koyyalagudem, Andhra Pradesh, 534312."));
		bankAdress.setBorder(0);
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAdress);
		
		PdfPTable statementInfo=new PdfPTable(2);
		
		PdfPCell startdate=new PdfPCell(new Phrase("Start Date :"+startDate));
		startdate.setBorder(0);
		PdfPCell statement=new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
		statement.setBorder(0);
		PdfPCell stopDate=new PdfPCell(new Phrase("End Date :"+endDate));
		stopDate.setBorder(0);
		PdfPCell customerInfo=new PdfPCell(new Phrase("Name of customer :"+user.getFirstName()+" "+user.getLastName()));
		customerInfo.setBorder(0);
		PdfPCell space=new PdfPCell();
		space.setBorder(0);
		PdfPCell adress=new PdfPCell(new Phrase("Address :"+user.getAddress()));
		adress.setBorder(0);
		
		statementInfo.addCell(startdate);
		statementInfo.addCell(statement);
		statementInfo.addCell(stopDate);
		statementInfo.addCell(customerInfo);
		statementInfo.addCell(space);
		statementInfo.addCell(adress);
		
		PdfPTable transactionTable=new PdfPTable(4);
		PdfPCell date=new PdfPCell(new Phrase("DATE "));
		date.setBackgroundColor(BaseColor.BLUE);
		date.setBorder(0);
		PdfPCell type=new PdfPCell(new Phrase("Transaction Type"));
		type.setBackgroundColor(BaseColor.BLUE);
		type.setBorder(0);
		PdfPCell tranAmount=new PdfPCell(new Phrase("AMOUNT"));
		tranAmount.setBackgroundColor(BaseColor.BLUE);
		tranAmount.setBorder(0);
		PdfPCell tranStatus=new PdfPCell(new Phrase("STATUS"));
		tranStatus.setBackgroundColor(BaseColor.BLUE);
		tranStatus.setBorder(0);
		
		
		
		transactinList.forEach(transaction->{
			transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionTable.addCell(new Phrase(transaction.getTransactionType()));
			transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionTable.addCell(new Phrase(transaction.getStatus()));
		});
		
		
		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionTable); 
		transactionTable.addCell(date);
		transactionTable.addCell(type);
		transactionTable.addCell(tranAmount);
		transactionTable.addCell(tranStatus);
		
		document.close();
		
		EmailDetails emailDetails=EmailDetails.builder()
				.recipient(user.getEmail())
				.subject("Your Account Statement")
				.messageBody("Kindly find your requested Account Statement attached!")
				.attachment(FILE)
				.build();
		emailService.sendEmailWithAttachment(emailDetails);
		
		return transactinList;
		
	}

}
