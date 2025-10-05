package com.example.weatherapi;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeatherResponse {
    private String city;
    private double temperature;
    private String conditions;
}
