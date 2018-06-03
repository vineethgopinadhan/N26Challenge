package com.n26.n26challenge.transactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.n26.n26challenge.transactions.beans.TransactionRequest;
import com.n26.n26challenge.transactions.entity.TransactionStatistics;
import com.n26.n26challenge.transactions.mapper.TransactionTimeManager;
import com.n26.n26challenge.transactions.service.TransactionService;
/**
 * 
 * @author vineeth
 *
 */
@RestController
public class TransactionsController {
	
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	TransactionTimeManager transactionTimeManager;
	
	@RequestMapping(value = "/statistics",
			method = RequestMethod.GET,
			produces = {"application/JSON"},
			headers = {"content-type=application/JSON"})
	@ResponseBody
	public TransactionStatistics get(){
		
		TransactionStatistics transactionStatistics = 
				transactionService.getStatistics();
		
		return transactionStatistics;
	}
	
	@RequestMapping(value = "/transactions",
					method = RequestMethod.POST,
					consumes = {"application/JSON"},
					headers = {"content-type=application/JSON"})
	@ResponseStatus(code = HttpStatus.CREATED)
	public void post(@RequestBody TransactionRequest transactionRequest){
		
		transactionService.saveTransaction(transactionRequest);
		
	}
}
