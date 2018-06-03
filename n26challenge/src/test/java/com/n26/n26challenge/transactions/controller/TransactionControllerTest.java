package com.n26.n26challenge.transactions.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.n26challenge.N26challengeApplicationTests;
import com.n26.n26challenge.transactions.beans.TransactionRequest;
import com.n26.n26challenge.transactions.entity.TransactionStatistics;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
							classes = N26challengeApplicationTests.class)
public class TransactionControllerTest {
	
	@Autowired
	TestRestTemplate restTemplate;
	
		
	@Test
	public void testSaveTransactionsValidTimeStamp(){
		
		long currentTime = System.currentTimeMillis();
		
		TransactionRequest transactiorequest = new TransactionRequest();
		transactiorequest.setAmount(10.00);
		transactiorequest.setTimestamp(currentTime);
		
		ResponseEntity<TransactionStatistics> responseEntity = 
				restTemplate.postForEntity("/transactions", transactiorequest, TransactionStatistics.class);
		
		assertEquals("The HTTP Response code should be 201 CREATED for successful retrieval ",
				HttpStatus.CREATED, responseEntity.getStatusCode());
		
	}
	
	@Test
	public void testSaveTransactionsInValidTimeStamp(){
		
				
		TransactionRequest transactiorequest = new TransactionRequest();
		transactiorequest.setAmount(10.00);
		
		//setting a time older than 60 seconds
		transactiorequest.setTimestamp(1527793118628L);
		
		ResponseEntity<TransactionStatistics> responseEntity = 
				restTemplate.postForEntity("/transactions", transactiorequest, TransactionStatistics.class);
		
		assertEquals("The HTTP Response code should be 204 NO CONTENT for successful retrieval ",
				HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
		
	}
	

	@Test
	public void testStatisticsClientResponse(){		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		
		ResponseEntity<TransactionStatistics> responseEntity = 
				restTemplate.exchange("/statistics",HttpMethod.GET,entity, TransactionStatistics.class);
		
		System.out.println(responseEntity.getStatusCode());
		
		assertEquals("The HTTP Response code should be 200 OK for successful retrieval ",
				HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	
}
