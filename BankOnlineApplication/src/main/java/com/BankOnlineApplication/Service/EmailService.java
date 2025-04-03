package com.BankOnlineApplication.Service;

import com.BankOnlineApplication.DTO.EmailDetails;

public interface EmailService {
	
	void sendMail(EmailDetails emailDetails);
	public void sendEmailWithAttachment(EmailDetails emailDetails);

}
