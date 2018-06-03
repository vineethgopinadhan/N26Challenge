package com.n26.n26challenge.transactions.entity;

public class Transaction{
	
	private double amount;
	
	private long transactionTime;
	
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(long transactionTime) {
		this.transactionTime = transactionTime;
	}

	@Override
	public String toString() {
		return "Transaction [amount=" + amount + ", transactionTime=" + transactionTime + "]";
	}

	
}
