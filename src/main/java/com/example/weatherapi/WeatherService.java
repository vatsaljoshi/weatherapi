package com.example.weatherapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class WeatherService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final WebClient webClient;

    public WeatherService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.webClient = WebClient.create();
    }

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherResponse getWeather(String city) {
        // Check Cache
        WeatherResponse cachedData = (WeatherResponse) redisTemplate.opsForValue().get(city);
        if (cachedData != null) return cachedData;

        // Fetch from External API
        String url = String.format("%s%s?key=%s", apiUrl, city, apiKey);
        var response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // You’ll parse JSON response properly here
        WeatherResponse weather = new WeatherResponse();
        weather.setCity(city);
        weather.setTemperature(28.5);  // mock until parsing added
        weather.setConditions("Sunny");

        // Cache Data for 12 Hours
        redisTemplate.opsForValue().set(city, weather, Duration.ofHours(12));

        return weather;
    }
}
