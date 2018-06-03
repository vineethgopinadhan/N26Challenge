package com.n26.n26challenge.transactions.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.n26challenge.N26challengeApplicationTests;
import com.n26.n26challenge.exceptions.OldTransactionException;
import com.n26.n26challenge.transactions.beans.TransactionRequest;
import com.n26.n26challenge.transactions.entity.TransactionStatistics;
import com.n26.n26challenge.transactions.mapper.TimeManager;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes = N26challengeApplicationTests.class)
public class TransactionServiceTest {
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	TimeManager timeManager;

	@Test
	public void testNoSavedTransactions() throws Exception{
		timeManager.increaseOffset();
		TransactionStatistics transactionStatistics = 
							transactionService.getStatistics();
		
		assertFalse(!transactionStatistics.isEmptyStatistics());
	}
	
	@Test(expected = OldTransactionException.class)
	public void testOldTransaction() 
					throws OldTransactionException{
		final TransactionRequest transactionRequest = new TransactionRequest();
		transactionRequest.setAmount(10);
		transactionRequest.setTimestamp(1527793118628L);
		
		transactionService.saveTransaction(transactionRequest);		
		
	}
	
	@Test
	public void testSameDatareturnWhenOnlyOneTransactionPresent() 
					throws OldTransactionException{
		
		timeManager.resetClock();
		
		final TransactionRequest transactionRequest = new TransactionRequest();
		transactionRequest.setAmount(10);
		transactionRequest.setTimestamp(System.currentTimeMillis());
		
		transactionService.saveTransaction(transactionRequest);		
		
		TransactionStatistics transactionStatistics = 
									transactionService.getStatistics();
		
		assertEquals(transactionStatistics.getSum(), transactionRequest.getAmount(), 0);
		assertEquals(transactionStatistics.getAvg(), transactionRequest.getAmount(), 0);
		assertEquals(transactionStatistics.getMin(), transactionRequest.getAmount(), 0);
		assertEquals(transactionStatistics.getMax(), transactionRequest.getAmount(), 0);
		assertEquals(transactionStatistics.getCount(), 1L);
		
	}
	
	
	@Test
	public void testStatisticsForValidTransactions() throws Exception{
		
		timeManager.increaseOffset();
		
		timeManager.stopClock();
		
		TransactionRequest transactionRequest =null;
		double sum = 0;
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		
		for (int i = 0; i < 10000; i++) {
			
			transactionRequest = new TransactionRequest();
			transactionRequest.setAmount(Math.random() * 1000);
			
			transactionRequest.setTimestamp(timeManager.getCurrentTimeStamp() + ( i * 10));
			
			sum = sum + transactionRequest.getAmount();
			max = Math.max(max, transactionRequest.getAmount());
			min = Math.min(min, transactionRequest.getAmount());
			
			transactionService.saveTransaction(transactionRequest);	
		}
		
		final double average = sum / 10000;
		
		final TransactionStatistics transactionStatistics = transactionService.getStatistics();
		
		final double epsilon = 0.001;
		assertEquals(transactionStatistics.getSum(), sum, epsilon);
		assertEquals(transactionStatistics.getAvg(), average, epsilon);
		assertEquals(transactionStatistics.getCount(), 10000);
		assertEquals(transactionStatistics.getMin(), min, epsilon);
		assertEquals(transactionStatistics.getMax(), max, epsilon);
		
		timeManager.continueClock();
		
	}
	
	@Test
	public void testStatisticsLastValidTransactionAfterTwoMinutes()
								throws OldTransactionException{
		
		timeManager.stopClock();		
		timeManager.increaseOffset();
		
		
		TransactionRequest transactionRequest =null;
		for (int i = 0; i < 10000; i++) {
			
			transactionRequest = new TransactionRequest();
			transactionRequest.setAmount(Math.random() * 1000);
			
			transactionRequest.setTimestamp(timeManager.getCurrentTimeStamp() +  i );
			transactionService.saveTransaction(transactionRequest);	
			
			
		}
		
		timeManager.increaseOffset();	
		
		final TransactionStatistics transactionStatistics = transactionService.getStatistics();
		long count = transactionStatistics.getCount();
		
		assertFalse(count > 0);
		
		timeManager.continueClock();
	}	
	
	
}
