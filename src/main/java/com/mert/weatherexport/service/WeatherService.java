package com.mert.weatherexport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mert.weatherexport.model.request.WeatherDataRequest;
import com.mert.weatherexport.model.response.WeatherDataResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@Service
public class WeatherService {

    @Value( "${weather.api.key}" )
    private String API_KEY;

    @Value( "${weather.api.url}" )
    private String API_URL;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;


    public WeatherService() {
        this.httpClient = HttpClientBuilder.create().build();
        this.objectMapper = new ObjectMapper();
    }

    public WeatherDataResponse getWeather(WeatherDataRequest weatherDataRequest) throws IOException, IOException {
        HttpGet request = new HttpGet(API_URL + "?q=" + weatherDataRequest.getCountryName() + "&appid=" + API_KEY);
        HttpResponse response = httpClient.execute(request);
        System.out.println("Response: " + response);

        Reader reader = new InputStreamReader(response.getEntity().getContent());
        return objectMapper.readValue(reader, WeatherDataResponse.class);
    }

}
