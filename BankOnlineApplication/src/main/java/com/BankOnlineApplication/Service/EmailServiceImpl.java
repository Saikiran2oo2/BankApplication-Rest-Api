package com.BankOnlineApplication.Service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.BankOnlineApplication.DTO.EmailDetails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService{
	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String senderEmail;

	@Override
	public void sendMail(EmailDetails emailDetails) {
		try {
			SimpleMailMessage mailMessage= new SimpleMailMessage();
			mailMessage.setFrom(senderEmail);
			mailMessage.setTo(emailDetails.getRecipient());
			mailMessage.setText(emailDetails.getMessageBody());
			mailMessage.setSubject(emailDetails.getSubject());
			
			javaMailSender.send(mailMessage);
			System.out.println("Email sent");
		}
		catch(MailException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void sendEmailWithAttachment(EmailDetails emailDetails) {
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper=new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(senderEmail);
			mimeMessageHelper.setTo(emailDetails.getRecipient());
			mimeMessageHelper.setText(emailDetails.getMessageBody());
			mimeMessageHelper.setSubject(emailDetails.getSubject());
			
			FileSystemResource file=new FileSystemResource(emailDetails.getAttachment());
			mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
			javaMailSender.send(mimeMessage);
			
			log.info(file.getFilename()+" has been sent to user "+emailDetails.getRecipient());
			
		}catch(MessagingException e)
		{
			throw new RuntimeException(e);
		}
		
	}
	
	

}
