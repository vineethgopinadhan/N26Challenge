package com.n26.n26challenge.transactions.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TransactionStatistics implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private double sum;
	private double avg;
	private double max;
	private double min;
	private long count;
		
	@JsonIgnore
	private long timestamp;
	
	public TransactionStatistics(){
		this.sum=0.0;
		this.avg=0.0;
		this.max=0.0;
		this.min=0.0;
		this.count=0;
	}
	
	public TransactionStatistics(double sum,double avg,double max,double min,long count, long timestamp){
		this.sum=sum;
		this.avg=avg;
		this.max=max;
		this.min=min;
		this.count=count;
		this.timestamp = timestamp; 
	}
	
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	@Transient
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	@JsonIgnore
	public boolean isEmptyStatistics(){
		
		if(this.sum ==0.0 && this.avg == 0.0 
				&& this.min==0.0 && this.max==0.0 && this.count== 0){
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "TransactionStatistics [sum=" + sum + ", avg=" + avg + ", max=" + max + ", min=" + min + ", count="
				+ count + ", timestamp=" + timestamp + "]";
	}		
	
	
}
