package com.example.weatherapp



data class DailyWeather(val dt:Long,val temp:Temp, val pressure:Int,val humidity:Int,
                        val weather:List<Description>
                        )

data class CurrentWeather(val dt:Long,val temp: Double, val pressure:Int,val humidity:Int,
                           val wind_speed:Double, val weather: List<Description>)
data class HourlyWeather(val dt:Long,val temp: Double, val pressure:Int,val humidity:Int,
                         val weather: List<Description>
                         )

data class CurrentWeatherDetails(val current: CurrentWeather)
data class DailyWeatherDetails(val daily: List<DailyWeather>)
data class HourlyWeatherDetails(val hourly: List<HourlyWeather>)

data class Temp(val day:Double,
                val min:Double,
                val max:Double
                )

data class Description(val description: String,val icon:String)