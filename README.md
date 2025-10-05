
# 🌤️ Weather API (Java + Spring Boot + Redis)

This project is a **Weather API** built with **Spring Boot** that fetches and returns real-time weather data from a **3rd-party weather API (Visual Crossing)**.  
It demonstrates how to:
- Work with **external APIs**
- Implement **caching** using Redis
- Manage **environment variables**
- Handle **errors and rate limiting**

---

## 🧠 Project Overview

The API accepts a **city name** (or code) as input, checks if the data is available in Redis cache, and:
- ✅ Returns cached data if available  
- 🌐 Fetches new data from the **Visual Crossing Weather API** if not cached  
- 💾 Stores the response in Redis for 12 hours (automatic expiration)

---

## ⚙️ Tech Stack

| Component | Technology |
|------------|-------------|
| Language | Java 17+ |
| Framework | Spring Boot |
| HTTP Client | WebClient (Reactive) |
| Cache | Redis |
| Rate Limiting | Bucket4j |
| Build Tool | Maven |
| Config Management | Environment Variables |

---

## 🗂️ Project Structure

```

weather-api/
├─ src/main/java/com/example/weatherapi/
│   ├─ controller/
│   │   └─ WeatherController.java
│   ├─ service/
│   │   └─ WeatherService.java
│   ├─ config/
│   │   └─ RedisConfig.java
│   ├─ model/
│   │   └─ WeatherResponse.java
│   └─ WeatherApiApplication.java
├─ src/main/resources/
│   ├─ application.properties
└─ pom.xml

````

---

## 🔑 Environment Variables

Set these environment variables (or define them in your `.env` file):

```bash
WEATHER_API_KEY=your_visualcrossing_api_key
REDIS_HOST=localhost
REDIS_PORT=6379
````

You can create a `.env` file in the root directory for local development:

```
WEATHER_API_KEY=xxxxxxxxxxxxxxxxxxxxxx
```

---

## ⚙️ Configuration (application.properties)

```properties
weather.api.url=https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/
weather.api.key=${WEATHER_API_KEY}

spring.redis.host=${REDIS_HOST:localhost}
spring.redis.port=${REDIS_PORT:6379}
```

---

## 🚀 Running the Application

### 1️⃣ Start Redis

If you have Docker:

```bash
docker run -d --name redis -p 6379:6379 redis
```

Or install Redis locally (see [https://redis.io/download](https://redis.io/download)).

### 2️⃣ Build & Run Spring Boot App

Using Maven:

```bash
mvn clean install
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

## 🌦️ Example API Usage

### Request

```
GET http://localhost:8080/api/weather/london
```

### Response

```json
{
  "city": "london",
  "temperature": 28.5,
  "conditions": "Sunny"
}
```

---

## 🧩 How It Works

1. User sends a request with a city name.
2. The application checks Redis cache:

    * ✅ If found → returns cached data.
    * ❌ If not found → calls **Visual Crossing API**.
3. The new data is cached in Redis for 12 hours (using `Duration.ofHours(12)`).
4. Proper error handling ensures graceful failure when:

    * The 3rd-party API is unreachable.
    * The city name is invalid.
    * Redis connection fails.

---

## 🛡️ Error Handling

| Case              | Response                    |
| ----------------- | --------------------------- |
| Invalid city      | `400 Bad Request`           |
| API unreachable   | `503 Service Unavailable`   |
| Redis unavailable | Fallback to direct API call |

---

## 🧱 Rate Limiting (Optional)

You can integrate **Bucket4j** to prevent abuse:

* Limit requests per IP per minute/hour.
* Return HTTP 429 (Too Many Requests) if limit exceeded.

---

## 🧪 Testing the API

You can use:

* **Postman**
* **cURL**

Example:

```bash
curl http://localhost:8080/api/weather/paris
```

---

## 📦 Future Enhancements

* [ ] Add pagination or history endpoints
* [ ] Integrate OpenWeatherMap as fallback API
* [ ] Add Docker Compose for Redis + App
* [ ] Add proper JSON parsing from Visual Crossing response
* [ ] Add unit/integration tests

---

## 🧾 License

This project is open-source.

---