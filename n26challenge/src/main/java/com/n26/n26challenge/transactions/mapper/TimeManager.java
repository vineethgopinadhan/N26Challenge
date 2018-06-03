package com.n26.n26challenge.transactions.mapper;

import org.springframework.stereotype.Component;

@Component
public interface TimeManager {	
	public long getCurrentTimeStamp();
	public long getOffSet();
	public void setOffSet(long offSet);
	public void increaseOffset();
	public void decreaseOffset();
	public void stopClock();
	public void continueClock();
	public void resetClock();
}
