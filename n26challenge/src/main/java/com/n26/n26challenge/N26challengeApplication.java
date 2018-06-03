package com.n26.n26challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.n26.n26challenge.transactions.mapper.TimeManager;
import com.n26.n26challenge.transactions.mapper.TransactionTimeManager;

@SpringBootApplication
public class N26challengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(N26challengeApplication.class, args);
	}
	
	
	public TimeManager getTimeManager(){
		return new TransactionTimeManager();
	}
	/**
	 * Only need to be uncommented to review loaded beans
	 */
	/*@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx){		
		return args -> {
			System.out.println("Inspecting beans : ");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
		};
	}*/
}
