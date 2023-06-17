package com.mert.weatherexport.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mert.weatherexport.model.request.WeatherDataRequest;
import com.mert.weatherexport.model.response.WeatherDataResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

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

    public byte[] exportExcel(String countryName) throws IOException {
        HttpGet request = new HttpGet(API_URL + "?q=" + countryName + "&appid=" + API_KEY);
        HttpResponse response = httpClient.execute(request);
        System.out.println("Response: " + response);

        Reader reader = new InputStreamReader(response.getEntity().getContent());
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser(reader);

        JsonNode jsonNode = objectMapper.readTree(jsonParser);
        System.out.println("Json node: " + jsonNode);

        // Create a new Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Weather Data");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Country Id");
        headerRow.createCell(1).setCellValue("Country Name");
        headerRow.createCell(2).setCellValue("Longitude");
        headerRow.createCell(3).setCellValue("Latitude");
        headerRow.createCell(4).setCellValue("Temperature");
        headerRow.createCell(5).setCellValue("Feels Like");
        headerRow.createCell(6).setCellValue("Humidity");


        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(jsonNode.get("id").asText());
        dataRow.createCell(1).setCellValue(jsonNode.get("name").asText());
        dataRow.createCell(2).setCellValue(jsonNode.get("coord").get("lon").asText());
        dataRow.createCell(3).setCellValue(jsonNode.get("coord").get("lat").asText());
        dataRow.createCell(4).setCellValue(jsonNode.get("main").get("temp").asText());
        dataRow.createCell(5).setCellValue(jsonNode.get("main").get("feels_like").asText());
        dataRow.createCell(6).setCellValue(jsonNode.get("main").get("humidity").asText());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        return outputStream.toByteArray();

    }

}
