package com.app.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.emails.EmailSenderService;

@CrossOrigin 	
@RestController
@RequestMapping(path="/email")
public class EmailController {
	
	@Autowired
    public EmailSenderService emailSender;
 
	@PutMapping("/{username}")
	public String resetpassword(@RequestParam String email) {
		try {
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Thành cong";
	}
}
