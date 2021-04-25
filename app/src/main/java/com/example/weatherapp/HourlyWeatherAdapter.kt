package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class HourlyWeatherAdapter(val weatherList:List<HourlyWeather>) :
        RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>() {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val temp: TextView = view.findViewById(R.id.hourlyTempShow)
        val hour : TextView = view.findViewById(R.id.hourT)
        val pressure: TextView = view.findViewById(R.id.hourlyPressureShow)
        val humidity: TextView = view.findViewById(R.id.hourlyHumidityShow)
        val description = view.findViewById<TextView>(R.id.hourlyDescShow)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_hourly_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = weatherList[position]

        val date = Date(item.dt * 1000)
            holder.hour.text = date.toLocaleString()

            holder.temp.text = ("Temp: " + item.temp.toString() + "°C")

            holder.pressure.text = ("Pressure: " + item.pressure.toString() + "hPa")
            holder.humidity.text = ("Humidity: " + item.humidity.toString() + "%" )
            holder.description.text = ("Description: " + item.weather[0].description)
        }




    override fun getItemCount(): Int = weatherList.size


}