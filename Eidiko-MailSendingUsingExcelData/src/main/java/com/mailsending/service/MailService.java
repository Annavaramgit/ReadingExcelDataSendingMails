package com.mailsending.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import com.mailsending.model.MailModel;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;

@Service
public class MailService {
	
	@Autowired
	private MailModel mailModel;
	
	@Autowired
	private JavaMailSender mailSender;
	
	//this is used for process the thymeleafe template
	@Autowired
	private TemplateEngine templateEngine;

	public void excelSheetUpload(InputStream inputStream,String subject,String body) throws IOException, MessagingException{
		
		//creates work book and get customer sheet in the work book
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheet("customer");
		
		//this loop iterates upto all rows present in the sheet
		for(int i=1;i<sheet.getPhysicalNumberOfRows();i++)
		{
			//get the row 
			XSSFRow row=sheet.getRow(i);
			if(row!=null)
			{
			//create two arraylists -one for to store emails data,second one is for remaining data in the sheet	
			List<String> emailList = new ArrayList<>();	
			List<String> remaminingRowsList = new  ArrayList<>();
			
			//this loop iterates all the cells in the current row
			for(int j=0;j<row.getLastCellNum();j++)
			{
				//get the cell
				XSSFCell cell = row.getCell(j);
				//get the cell value covertion to string format
				String cellContent = String.valueOf(cell);
				//to know that cell title for to add email arraylist or remaining array list,getColumn() method used for that
				String columnName = getColumnName(sheet.getRow(0).getCell(j));
				
				//if the cell title is customer_mail then add in mails arraylist other wise another arraylist
				 if(columnName.equalsIgnoreCase("customer_mail")) 
				 {
					 emailList.add(cellContent);
				 }
				 else
					 remaminingRowsList.add(cellContent);
			}
			System.out.println("========"+emailList);
			System.out.println(";;;;;"+remaminingRowsList);
			//after all to pass two arraylists and sub,body to sendMail whcih sends mails
			sendMail(emailList,remaminingRowsList,subject,body);
			}
			
		}
		
		
	}
	
	
	//this method is for to know cell title 
	 private String getColumnName(XSSFCell cell) {
	        if (cell != null) {
	            return cell.getStringCellValue().trim();
	        }
	        return "";
	    }
	 
	 //this method is for sends the mails
	 public void sendMail(List<String>to,List<String> remaminingRowsList,String subject,String body1) throws MessagingException
	 {
		 if(to!=null && !to.isEmpty() )
		 {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,true);
			
			String body =body1;
			
			// Iterate over each row value and replace placeholders in the email body
			for (int i = 0; i < remaminingRowsList.size(); i++) {
			    String placeHolder = "{" + i + "}";
			    String cellValue = remaminingRowsList.get(i).toString();
			    body = body.replace(placeHolder, cellValue);
			   
			}
			 System.out.println("-----------------------"+body);
			// Set email details and send email outside the loop (after placeholder replacement)
			helper.setTo(to.get(0));
			helper.setSubject(subject);
			helper.setText(body);

			mailSender.send(message);

			}
			
			
		 }
	 
	//this method used for sends mails using thymleaf template 
 public void sendMailMethod(MailModel mailModel) throws MessagingException
 {
	 MimeMessage message = mailSender.createMimeMessage();
	 MimeMessageHelper helper = new MimeMessageHelper(message,true);
	 
	 String toAdress = mailModel.getTo();
	 String subject = mailModel.getSubject();
	 String body    = mailModel.getBody();
	
	 //below 3 lines used for thymleafe (mail-template.html wroted in template folder)
	 Context context = new Context();
	 context.setVariable("content",body);
	 String processedString = templateEngine.process("mail-template.html", context);
	 
	 
	 
	 helper.setTo(toAdress);
	 helper.setSubject(subject);
	 helper.setText(processedString,true);
	// helper.setText(body);
	// helper.setFrom("satya123.eidiko@outlook.com");
	 mailSender.send(message);
 }
	 
	 
	 }
//}
