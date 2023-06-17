package com.mert.weatherexport.controller;

import com.mert.weatherexport.model.request.WeatherDataRequest;
import com.mert.weatherexport.model.response.WeatherDataResponse;
import com.mert.weatherexport.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    public final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping
    public ResponseEntity<WeatherDataResponse> getWeatherDataCountry(@RequestBody WeatherDataRequest weatherDataRequest) throws IOException {
        return ResponseEntity.ok(weatherService.getWeather(weatherDataRequest));
    }


}
