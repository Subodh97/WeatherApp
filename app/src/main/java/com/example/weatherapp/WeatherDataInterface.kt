package com.example.weatherapp

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import retrofit2.converter.gson.GsonConverterFactory

interface WeatherDataInterface {

    @GET("data/2.5/onecall")
    fun getDailyWeather(@Query("lat")lat:Double,
                   @Query("lon")lon:Double,
                   @Query("exclude")exclude:String,
                   @Query("units")units:String,
                   @Query("appid")key:String): Call<DailyWeatherDetails>

    @GET("data/2.5/onecall")
    fun getCurrentWeather(@Query("lat")lat:Double,
                        @Query("lon")lon:Double,
                        @Query("exclude")exclude:String,
                        @Query("units")units:String,
                        @Query("appid")key:String): Call<CurrentWeatherDetails>


    @GET("data/2.5/onecall")
    fun getHourlyWeather(@Query("lat")lat:Double,
                        @Query("lon")lon:Double,
                        @Query("exclude")exclude:String,
                        @Query("units")units:String,
                        @Query("appid")key:String): Call<HourlyWeatherDetails>

//    @GET
//    fun getImage(@Url url:String): Call<ResponseBody>

    companion object{
        val BASE_URL = "https://api.openweathermap.org/"


        fun getInstance(): WeatherDataInterface{
            val builder =  Retrofit.Builder()
            builder.addConverterFactory(GsonConverterFactory
                .create())
            builder.baseUrl(BASE_URL)

            val retrofit = builder.build()
            return retrofit.create(WeatherDataInterface::class.java)

        }



    }


}