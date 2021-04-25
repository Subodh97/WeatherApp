package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HourlyFragment : Fragment() {
    private var columnCount = 1
    private var lat = 0.0
    private var long = 0.0

    lateinit var rView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            lat = it.getDouble(DailyFragment.ARG_LAT)
            long = it.getDouble(DailyFragment.ARG_LONG)

        }
        val key = resources.getString(R.string.appid)
        val request =
            WeatherDataInterface.getInstance().getHourlyWeather(lat, long,
                "minutely,current,daily", "metric", key)
        request.enqueue(WeatherCallback())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hourly, container, false)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                //adapter = WeatherListRecyclerViewAdapter(DummyContent.ITEMS)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rView = view.findViewById(R.id.rViewHourly)

        super.onViewCreated(view, savedInstanceState)
    }
    companion object {

        // TODO: Rename and change types and number of parameters

        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_LAT = "lat"
        const val ARG_LONG = "long"

        @JvmStatic
        fun newInstance(columnCount: Int, lat:Double, long:Double) =
            HourlyFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putDouble(DailyFragment.ARG_LAT,lat)
                    putDouble(DailyFragment.ARG_LONG,long)
                }
            }
    }

    inner class WeatherCallback: Callback<HourlyWeatherDetails> {
        override fun onResponse(call: Call<HourlyWeatherDetails>,
                                response: Response<HourlyWeatherDetails>) {

            if(response.isSuccessful){
                val weatherDetails =  response.body()
                Log.d("HourlyFragment","List:$weatherDetails")

                weatherDetails?.hourly?.let{
                    rView.adapter = HourlyWeatherAdapter(it)

                }

            }
            else{
                Log.d("HourlyFragment","$response")
            }

        }

        override fun onFailure(call: Call<HourlyWeatherDetails>, t: Throwable) {
            Toast.makeText(context,
                "failed to get weather: ${t.message}", Toast.LENGTH_LONG).show()
            Log.d("HourlyFragment","error: ${t.message}")
        }
    }

}