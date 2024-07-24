package com.mailsending.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component 
public class MailModel {

	private String to;
	private String subject;
	private String body;
	
	
}
