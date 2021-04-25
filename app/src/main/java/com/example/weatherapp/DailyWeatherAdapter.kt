package com.example.weatherapp

import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import com.bumptech.glide.Glide

class DailyWeatherAdapter(val weatherList: List<DailyWeather>) :
        RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder>(){


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val date:TextView = view.findViewById(R.id.dateT)
        val icon:ImageView = view.findViewById(R.id.dailyWeatherIcon)

        val temp: TextView = view.findViewById(R.id.dailyTempShow)
        val temp_min: TextView = view.findViewById(R.id.dailyTempMinShow)
        val temp_max: TextView = view.findViewById(R.id.dailyTempMaxShow)
        val pressure:TextView = view.findViewById(R.id.dailyPressureShow)
        val humidity:TextView = view.findViewById(R.id.dailyHumidityShow)
        val description: TextView = view.findViewById(R.id.dailyDescShow)

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_daily_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = weatherList[position]

        val date = Date(item.dt * 1000)
        //Log.d("DailyWeather","${item.dt}")

        holder.date.text = (date.toLocaleString())

            holder.temp.text = ("Temp: " + item.temp.day.toString() +"°C")
            holder.temp_min.text = ("Min Temp: "+ item.temp.min.toString() + "°C")
            holder.temp_max.text = ("Max Temp: " + item.temp.max.toString()+"°C")
            holder.pressure.text = ("Pressure" + item.pressure.toString()+"hPa")
            holder.humidity.text = ("Humidity: " + item.humidity.toString()+"%")
            holder.description.text = ("Description: " +item.weather[0].description)

        val imgUrl = "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png"
        Glide.with(holder.itemView.context)
                .load(Uri.parse(imgUrl))
                .into(holder.icon)


    }

    override fun getItemCount(): Int = weatherList.size


}