package com.iqnext.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.iqnext.tasklet.CreateWeatherData;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

	
	@Autowired
    private JobBuilderFactory jobBuilders;

    @Autowired
    private StepBuilderFactory stepBuilders;
    
    @Bean
    public Job customerReportJob() {
    	System.out.println("Hello JOB");
        return jobBuilders.get("customerReportJob")
        		.incrementer(new RunIdIncrementer())
            .start(createDataStep())
           // .next(saveDataStep())
            .build();
    }
    
    @Bean
    public Step createDataStep() {
    	System.out.println("Hello STEP");
    	return stepBuilders.get("CreateData")
    			.tasklet(new CreateWeatherData())
    			.build();
	}



  
    
   
}
