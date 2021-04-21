package com.app.demo.emails;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class EmailSenderService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	public void sendSimpleEmail(String toEmail,
								String fullName, String password) throws  Exception {
		 MimeMessage message = mailSender.createMimeMessage();
		 boolean multipart = true;
		 MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
		message.setContent("<!DOCTYPE html>"
				+"<html>"
				+ "<h3>Major Sales Management System </h3>"
				+ "	<span>Default password is: "+ password+"</span>"
				+"</html>","text/html");
		helper.setFrom("cp.salesmgt@gmail.com");
		helper.setTo(toEmail);
		message.setSubject(fullName+", your password is: "+password+". Please keep this information secure to access the system.");
		mailSender.send(message);
	}
	
}
