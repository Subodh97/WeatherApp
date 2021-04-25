package com.example.weatherapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CurrentFragment : Fragment() {
    private var columnCount = 1
    private var lat = 0.0
    private var long = 0.0

    lateinit var currTemp : TextView
    lateinit var currPressure: TextView
    lateinit var currHumidity : TextView
    lateinit var currWindSpeed : TextView
    lateinit var currDescription: TextView
    lateinit var currIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            lat = it.getDouble(DailyFragment.ARG_LAT)
            long = it.getDouble(DailyFragment.ARG_LONG)

        }



        val key = resources.getString(R.string.appid)
        val request =
            WeatherDataInterface.getInstance().getCurrentWeather(lat, long,
                "minutely,hourly,daily", "metric", key)
        request.enqueue(WeatherCallback())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_current, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       currTemp = view.findViewById(R.id.currTempShow)
        currPressure = view.findViewById(R.id.currPresShow)
        currHumidity = view.findViewById(R.id.currHumidShow)
        currWindSpeed = view.findViewById(R.id.currWindspdShow)
        currDescription = view.findViewById(R.id.currDescShow)
        currIcon = view.findViewById(R.id.currWeatherIcon)

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        // TODO: Rename and change types and number of parameters

        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_LAT = "lat"
        const val ARG_LONG = "long"

        @JvmStatic
        fun newInstance(columnCount: Int,lat:Double, long:Double) =
            CurrentFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putDouble(DailyFragment.ARG_LAT,lat)
                    putDouble(DailyFragment.ARG_LONG,long)
                }
            }
    }

    inner class WeatherCallback: Callback<CurrentWeatherDetails> {
        override fun onResponse(call: Call<CurrentWeatherDetails>,
                                response: Response<CurrentWeatherDetails>
        ) {

            if(response.isSuccessful){
                val weatherDetails =  response.body()
                Log.d("CurrentFragment","List:$response")

                weatherDetails?.current?.let{
                        currTemp.text = ("Temp: " + it.temp.toString()+"°C")
                        currPressure.text = ("Pressure: "+it.pressure.toString()+" hPa")
                        currHumidity.text = ("Humidity: "+it.humidity.toString() +"%")
                        currWindSpeed.text = ("Wind Speed: "+it.wind_speed.toString() +"m/s")
                        currDescription.text = ("Description: "+it.weather[0].description)

                    val imgUrl = "https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png"
                    Glide.with(this@CurrentFragment)
                        .load(Uri.parse(imgUrl))
                        .into(currIcon)

                }

            }
            else{
                Log.d("CurrentFragment","$response")
            }

        }

        override fun onFailure(call: Call<CurrentWeatherDetails>, t: Throwable) {
            Toast.makeText(context,
                "failed to get weather: ${t.message}", Toast.LENGTH_LONG).show()
            Log.d("CurrentFragment","error: ${t.message}")
        }
    }

}