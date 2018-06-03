package com.n26.n26challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.n26.n26challenge.transactions.mapper.TestTimeManager;
import com.n26.n26challenge.transactions.mapper.TimeManager;

@SpringBootApplication
public class N26challengeApplicationTests {
	
	public static void main(String[] args) {
		SpringApplication.run(N26challengeApplicationTests.class, args);
	}
	
	@Bean
	@Primary
	public TimeManager getTimeManager(){
		return new TestTimeManager();
	}
}
