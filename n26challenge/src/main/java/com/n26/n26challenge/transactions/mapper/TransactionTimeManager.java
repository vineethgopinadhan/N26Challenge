package com.n26.n26challenge.transactions.mapper;

import org.springframework.stereotype.Component;

@Component
public class TransactionTimeManager implements TimeManager {

	long offSet;
	
	
	public long getOffSet() {
		return offSet;
	}


	public void setOffSet(long offSet) {
		this.offSet = offSet;
	}
	
	
	public void increaseOffset(){
		this.offSet = this.offSet + 120000;
	}
	
	public void decreaseOffset(){
		this.offSet = this.offSet - 120000;
	}
	
	public long getCurrentTimeStamp() {		
		return System.currentTimeMillis();
	}
	public void stopClock(){};
	public void continueClock(){};
	public void resetClock(){};
}
