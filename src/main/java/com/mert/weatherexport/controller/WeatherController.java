package com.mert.weatherexport.controller;

import com.mert.weatherexport.model.request.WeatherDataRequest;
import com.mert.weatherexport.model.response.WeatherDataResponse;
import com.mert.weatherexport.service.WeatherService;
import org.apache.poi.util.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    public final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping
    public ResponseEntity<WeatherDataResponse> getWeatherDataCountry(@RequestBody WeatherDataRequest weatherDataRequest) throws IOException {
        return ResponseEntity.ok(weatherService.getWeather(weatherDataRequest));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> getWeatherDataCountryExcel(@RequestParam String countryName) throws IOException {

        byte[] content = weatherService.exportExcel(countryName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "filename.xlsx");

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }


}
