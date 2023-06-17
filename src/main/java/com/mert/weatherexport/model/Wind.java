package com.mert.weatherexport.model;

import lombok.Data;

@Data
public class Wind{
    public double speed;
    public int deg;
    public double gust;
}