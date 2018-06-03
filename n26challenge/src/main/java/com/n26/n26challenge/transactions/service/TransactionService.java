package com.n26.n26challenge.transactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.n26challenge.exceptions.OldTransactionException;
import com.n26.n26challenge.transactions.beans.TransactionRequest;
import com.n26.n26challenge.transactions.entity.Transaction;
import com.n26.n26challenge.transactions.entity.TransactionStatistics;
import com.n26.n26challenge.transactions.mapper.TimeManager;
import com.n26.n26challenge.transactions.mapper.TransactionMapper;

/**
 * 
 * @author Vineeth
 *
 */
@Service
public class TransactionService {
		
	@Autowired
	TransactionMapper transactionMapper;
	
	private final TransactionStatistics[] transactionStatistics;
	
	private  static final int TRANSACTION_TIME_TO_CONSIDER_MILLIS =60000;	
	private  static final int BUCKETSIZE_IN_MILLIS = 1000;
	
	private final Object transactionLock = new Object();
	
	@Autowired
	TimeManager timeManager;	
	/**
	 * 
	 */
	TransactionService(){
		
		this.transactionStatistics = new TransactionStatistics[TRANSACTION_TIME_TO_CONSIDER_MILLIS/BUCKETSIZE_IN_MILLIS]; // calculate array size
	}
	
	/**
	 * Main method which does saving individual transaction
	 * at a specific TransactionStatistics index 
	 * @param transactionRequest
	 * @return
	 */
	public Transaction saveTransaction(TransactionRequest transactionRequest)
				throws OldTransactionException{
		
		//Check if transaction is older than 60 seconds
		//Return 204 response code
		if(!validateTimeStamp(transactionRequest.getTimestamp())){
			throw new OldTransactionException();
		}
		
		//Invoke map to get Transaction entity - simple converter
		Transaction transaction = null;
		transaction = transactionMapper.mapTransactionRequestToTransaction(transactionRequest);		
		
		//construct index for the statistics array
		//The transaction data will be merged in case of collisions , if time stamp is valid , in the next step
		int index = (int)((transactionRequest.getTimestamp()/BUCKETSIZE_IN_MILLIS) % transactionStatistics.length);
		
		//save last 60 seconds transaction to transactionStatistics array
		transactionStatistics[index] = calculateStatistics(transaction,transactionStatistics[index]);
		
			
		return transaction;
	}
	
	/**
	 * 
	 * @param transaction
	 * @param transactionStatisticsCurrent
	 * @return
	 */
	public TransactionStatistics calculateStatistics(Transaction transaction,TransactionStatistics transactionStatisticsCurrent){
		
		//TO-DO Entire logic need to be modified to last 60 seconds
		synchronized(transactionLock){
			
			//current transaction amount
			double amount = transaction.getAmount();
			
			//Fresh transaction statistics object is created and returned when there are no other transaction present in the current index 
			// OR  the transaction time stamp at the current location is older.
			if(null == transactionStatisticsCurrent || !validateTimeStamp(transactionStatisticsCurrent.getTimestamp())){
				return new TransactionStatistics(amount,amount,amount,amount,1,transaction.getTransactionTime());
			}					
			
			//combining transaction statistics if there is already a valid transaction statistics at the index
			// "valid" indicates - a statistics with in 60 seconds
			transactionStatisticsCurrent = 
					combineTransactionStatistics(amount,transaction,transactionStatisticsCurrent);			
			
		}
		
		return transactionStatisticsCurrent;
	}
	
	/**
	 * Invoked during saveTransaction .
	 * Calculate statistics based on a Transaction input. 
	 * @param amount
	 * @param transaction
	 * @param combinedStatistics
	 * @return
	 */
	public TransactionStatistics combineTransactionStatistics(double amount,Transaction transaction,TransactionStatistics combinedStatistics){
		
		//merge sum with the existing statistics
		double transactionSum  = combinedStatistics.getSum() + amount;
		combinedStatistics.setSum(transactionSum);
		
		//get latest transaction count value from statistics object present at current index
		long transactionCount = combinedStatistics.getCount();
		
		//get max value from statistics object present at the current index
		double maxAmount  = combinedStatistics.getMax();		
		if(amount > maxAmount){
			combinedStatistics.setMax(amount);
		}
		
		double minAmount = combinedStatistics.getMin();
		
		if(amount < minAmount){
			combinedStatistics.setMin(amount);
		}
		
		//set transaction count
		// Adding 1 to include current transaction 
		combinedStatistics.setCount(transactionCount+1);
		
		//calculate and set new average value
		double transactionAverage = transactionSum / combinedStatistics.getCount();
		combinedStatistics.setAvg(transactionAverage);
		
		combinedStatistics.setTimestamp(transaction.getTransactionTime());
		
		return combinedStatistics;
	}
	
	/**
	 * Iterates thorough TransactionStatistics array and
	 * invokes mergeAndRetrieveStatistics method to calculate final statistics values.
	 * @return
	 */
	public TransactionStatistics getStatistics(){
		
		TransactionStatistics transactionStatisticsLatest = new TransactionStatistics();
		
		long currentTime = timeManager.getCurrentTimeStamp();
		
		//merge all valid statistics 
		//ignore those indexes which has invalid time stamp
		synchronized(transactionLock){
			
			for (int i = 0; i < transactionStatistics.length; i++) {
				
				if(null!= transactionStatistics[i]){
					
					if(validateTimeStamp(transactionStatistics[i].getTimestamp())){
						transactionStatisticsLatest =
							mergeAndRetrieveStatistics(currentTime,transactionStatistics[i],transactionStatisticsLatest);
					}
				}
				
			}
		}
				
		return transactionStatisticsLatest;
	}
	
	/**
	 * Main method that calculate final statistics between two TransactionStatistics.
	 * @param currentTransactionStatistics
	 * @param allOtherTransactionStatistics
	 * @return
	 */
	private TransactionStatistics mergeAndRetrieveStatistics
								(long currentTime ,TransactionStatistics currentTransactionStatistics,
										TransactionStatistics mergedTransactionStatistics){
		
		
		//Merging all valid trasnsaction statistics values in the array
		if(mergedTransactionStatistics.getTimestamp() < currentTime ){
			
			double sum = currentTransactionStatistics.getSum() + mergedTransactionStatistics.getSum();
			mergedTransactionStatistics.setSum(sum);						
			
			double avg = sum / (currentTransactionStatistics.getCount() + mergedTransactionStatistics.getCount());
			mergedTransactionStatistics.setAvg(avg);
			
			double max = currentTransactionStatistics.getMax();
			
			if(max > mergedTransactionStatistics.getMax() ){
				mergedTransactionStatistics.setMax(max);
			}
			
			double min = currentTransactionStatistics.getMin();
			
			if(0== mergedTransactionStatistics.getCount() || min < mergedTransactionStatistics.getMin()){
				mergedTransactionStatistics.setMin(min);
			}
			
			long count = currentTransactionStatistics.getCount() + mergedTransactionStatistics.getCount();
			mergedTransactionStatistics.setCount(count);
			
		}	
		
		return mergedTransactionStatistics;
	}
	/**
	 * 
	 * @param transactionRequest
	 * @return
	 * @throws OldTransactionException
	 */
	public boolean validateTimeStamp(long timestamp){
		
		long currentTime = timeManager.getCurrentTimeStamp();
		
		//Finding  difference in seconds between current time and the input time
		long timeDiff  = 
				(currentTime - timestamp ) /1000 ;
		
		//return if the transaction in request is older than 59 seconds
		if(timeDiff > 59){
			return false;
		}
		
		return true;
	}
}
