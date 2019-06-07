package com.revature.utils;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class CognitoRestTemplate {
	
	private Logger logger = Logger.getRootLogger();
	
	@Value("${cognito_url}")
	private String cognitoURL;
	

	private final String registerUrl = "/cognito/users";
	private final String authUrl = "/cognito/auth";
	
//	@Value("${spring.profiles}")
//	private String stage;


	
	public  ResponseEntity<String> registerUser(String email,String token) {
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.AUTHORIZATION, token);
		JSONObject reqJSON = new JSONObject();
		reqJSON.put("email", email);
		String url = cognitoURL + registerUrl;
		logger.info("Registering user to the following link: " + url);
		HttpEntity<String> entity = new HttpEntity<String>(reqJSON.toString(),headers);
		System.out.println(entity.getHeaders());
		System.out.println(entity.getBody());
		System.out.println(entity.toString());
		try{
			return rt.exchange(url ,HttpMethod.POST, entity , String.class );			
		}catch(HttpClientErrorException e) {
			System.out.print(e);
			return new ResponseEntity<String>(HttpStatus.CONFLICT);	
		}
	}
	
	public ResponseEntity<String> checkAuth(String token) {
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, token);
		String url= cognitoURL + authUrl;
		System.out.println("Checking user against the following url: " + url);
		System.out.println("Checking the following token: " + token);
		HttpEntity<String> entity = new HttpEntity<String>("",headers);
		try{
			return rt.exchange(url,HttpMethod.GET ,entity , String.class );
		}catch(HttpClientErrorException e) {
			System.out.print("Error is " + e);
			return new ResponseEntity<String>(HttpStatus.CONFLICT);	
		}
	}
	
	
	
}
