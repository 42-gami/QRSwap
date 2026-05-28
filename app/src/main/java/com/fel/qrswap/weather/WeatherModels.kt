package com.fel.qrswap.weather

data class WeatherResponse(
    val current: Current
)

data class Current(
    val time: String,
    val interval: Int,
    val temperature_2m: Double,
    val is_day: Int,
    val rain: Double,
    val wind_speed_10m: Double
)