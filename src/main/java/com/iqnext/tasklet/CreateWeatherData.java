package com.iqnext.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.iqnext.model.WeatherData;
import com.iqnext.repo.WeatherDataRepo;

import reactor.core.publisher.Mono;

public class CreateWeatherData implements Tasklet {

	Logger LOG = LoggerFactory.getLogger(CreateWeatherData.class);
	public WeatherDataRepo weatherDataRepo;
	
	
	public String url;
	
	public CreateWeatherData(WeatherDataRepo weatherDataRepo, String url) {
		// TODO Auto-generated constructor stub
		this.weatherDataRepo = weatherDataRepo;
		this.url = url;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		LOG.debug("CreateWeatherData taskelet Started");
		//WeatherDataRepo weatherDataRepo = new WeatherDataRepo();
		try {
			LOG.debug("Fetching Data from WeatherAPI");
			WebClient client = WebClient.builder() 
					  .baseUrl(url+"/getWeather")
					  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
					 // .defaultUriVariables(Collections.singletonMap("/getWeather", "http://localhost:3000"))
					  .build();
			ResponseEntity<WeatherData> response = client.get()
				    .retrieve() 
				    .onStatus(
				        status -> status.value() == 401,
				        clientResponse -> Mono.empty()
				    )
				    .toEntity(WeatherData.class)
				    .block();
			LOG.debug("DATA Fetched from WeatherAPI");
			
			try {
				weatherDataRepo.save(response.getBody());
				//weatherDataRepo.save(response.getBody());
				LOG.debug("DATA Written to Database Successfully");
			}catch(Exception e) {
				LOG.error(e.toString());
			
				
			}
		}catch(Exception e) {
			LOG.error(e.toString());
		}
		
		return RepeatStatus.FINISHED;
	}

}
