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
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mailsending.model.MailModel;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	
	@Autowired
	private MailModel mailModel;
	
	@Autowired
	private JavaMailSender mailSender;

	public void excelSheetUpload(InputStream inputStream,String subject,String body) throws IOException, MessagingException{
		
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheet("customer");
		
		for(int i=1;i<sheet.getPhysicalNumberOfRows();i++)
		{
			XSSFRow row=sheet.getRow(i);
			if(row!=null)
			{
			List<String> emailList = new ArrayList<>();	
			List<String> remaminingRowsList = new  ArrayList<>();
			
			for(int j=0;j<row.getLastCellNum();j++)
			{
				XSSFCell cell = row.getCell(j);
				String cellContent = String.valueOf(cell);
				 String columnName = getColumnName(sheet.getRow(0).getCell(j));
				 
				 if(columnName.equalsIgnoreCase("customer_mail")) 
				 {
					 emailList.add(cellContent);
				 }
				 else
					 remaminingRowsList.add(cellContent);
			}
			sendMail(emailList,remaminingRowsList,subject,body);
			}
			
		}
		
		
	}
	
	
	
	 private String getColumnName(XSSFCell cell) {
	        if (cell != null) {
	            return cell.getStringCellValue().trim();
	        }
	        return "";
	    }
	 
	 
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
	 }
//}
