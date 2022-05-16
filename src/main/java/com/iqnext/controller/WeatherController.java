package com.iqnext.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
	
	
	@GetMapping(path="/")
	public String getWeather() {
		return "Hello";
	}

}
