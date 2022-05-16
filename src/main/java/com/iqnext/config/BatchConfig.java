package com.iqnext.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.iqnext.repo.WeatherDataRepo;
import com.iqnext.tasklet.CreateWeatherData;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

	
	@Autowired
    private JobBuilderFactory jobBuilders;

    @Autowired
    private StepBuilderFactory stepBuilders;
    
    @Autowired
    WeatherDataRepo weatherDataRepo;
    
    @Value("${data.api.url}")
    public String url;
    
    @Bean
    public Job customerReportJob() {
    	//System.out.println("Hello JOB");
        return jobBuilders.get("customerReportJob")
        		.preventRestart()
        		.incrementer(new RunIdIncrementer())
            .start(createDataStep()) 
            .build()
            ;
    }
    
    @Bean
    public Step createDataStep() {
    	//System.out.println("Hello STEP");
    	return stepBuilders.get("CreateData")
    			.tasklet(new CreateWeatherData(weatherDataRepo, url))
    			.build();
	}



  
    
   
}
