package com.fel.qrswap.weather

data class CurrentWeather(
    val temperature_2m: Double,
    val rain: Double,
    val wind_speed_10m: Double
)

data class WeatherResponse(
    val current: CurrentWeather
)