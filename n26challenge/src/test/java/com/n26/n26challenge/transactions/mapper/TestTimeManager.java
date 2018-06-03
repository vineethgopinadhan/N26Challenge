package com.n26.n26challenge.transactions.mapper;

import org.springframework.stereotype.Component;

@Component
public class TestTimeManager extends TransactionTimeManager{
	
	private long offSet = 0;
	private long stopedTimeStamp = 0;
	
	private boolean isTimeStopped = false;
	
	public TestTimeManager(){
	}
	
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
	
	@Override
	public long getCurrentTimeStamp() {
		
		if(this.isTimeStopped){
			return this.offSet + this.stopedTimeStamp;
		}else{
			return System.currentTimeMillis() + this.offSet;
		}
	}
	
	public void resetClock(){
		this.offSet = 0;
		this.isTimeStopped = false;
	}
	
	@Override
	public void stopClock() {
		this.stopedTimeStamp = super.getCurrentTimeStamp();
		this.isTimeStopped = true;
	}
	
	public void continueClock() {
		this.isTimeStopped = false;
	}
	
	public boolean isTimeStopped() {
		return isTimeStopped;
	}	
}
