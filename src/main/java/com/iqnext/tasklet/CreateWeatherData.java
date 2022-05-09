package com.iqnext.tasklet;

import java.util.Collections;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.iqnext.model.WeatherData;
import com.iqnext.repo.WeatherDataRepo;

import reactor.core.publisher.Mono;

public class CreateWeatherData implements Tasklet {

	@Autowired 
	public WeatherDataRepo weatherDataRepo;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("TEST>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>1");
		//WeatherDataRepo weatherDataRepo = new WeatherDataRepo();
		try {
			WebClient client = WebClient.builder()
					  .baseUrl("http://localhost:3000/getWeather")
					  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
					  .defaultUriVariables(Collections.singletonMap("/getWeather", "http://localhost:3000"))
					  .build();
			ResponseEntity<WeatherData> response = client.get()
				    .retrieve()
				    .onStatus(
				        status -> status.value() == 401,
				        clientResponse -> Mono.empty()
				    )
				    .toEntity(WeatherData.class)
				    .block();
			System.out.println("HUMIDITY: "+response.getBody().getHumidity());
			
			try {
				weatherDataRepo.save(response.getBody());
				//weatherDataRepo.save(response.getBody());
				System.out.println("DATA SAVED SUCCESS FULLY");
			}catch(Exception e) {
				e.printStackTrace();
			
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return RepeatStatus.FINISHED;
	}

}
