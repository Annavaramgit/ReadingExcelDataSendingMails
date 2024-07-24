package com.mailsending.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mailsending.model.MailModel;
import com.mailsending.service.MailService;

import jakarta.mail.MessagingException;

@RestController
public class Mail_Controller {

	@Autowired
	private MailService mailService;
	
	@Autowired
	private MailModel mailModel;
	
	//this method is for read excel file and send mails with place holder replacement 
	  @PostMapping("/mailing")
	    public String uploadExcel(@RequestParam("file") MultipartFile file, @RequestParam("subject") String subject,@RequestParam("body") String body) throws IOException, MessagingException {
	        try {
	            mailService.excelSheetUpload(file.getInputStream(), subject,body);
	        } catch (Exception e) {
	            System.out.println(e.toString());
	        }
	        return "Sended....";
	    }
	  
	  //this is for sends mails with thymleafe template ,i sent mails through gmail , outlook also check in application.yml file
	  @PostMapping("/sendmail")
	  public String sendMails(@RequestBody MailModel mailModel) throws MessagingException{
		  
		  mailService.sendMailMethod(mailModel);
		  
		return "sended";
		  
		  
	  }
	
	
}
