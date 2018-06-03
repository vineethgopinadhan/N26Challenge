package com.n26.n26challenge.transactions.beans;

import java.io.Serializable;

public class TransactionRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	double amount;
	Long timestamp;
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "Transaction [amount=" + amount + ", timestamp=" + timestamp + "]";
	}
	
	
}
