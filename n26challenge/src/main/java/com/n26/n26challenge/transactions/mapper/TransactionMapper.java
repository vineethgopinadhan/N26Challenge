package com.n26.n26challenge.transactions.mapper;

import org.springframework.stereotype.Component;

import com.n26.n26challenge.transactions.beans.TransactionRequest;
import com.n26.n26challenge.transactions.entity.Transaction;

@Component
public class TransactionMapper {
	
	public Transaction mapTransactionRequestToTransaction(TransactionRequest transactionRequest){
		Transaction transaction = new Transaction();
		
		transaction.setAmount(transactionRequest.getAmount());
		transaction.setTransactionTime(transactionRequest.getTimestamp().longValue());
		
		return transaction;
	}
}
