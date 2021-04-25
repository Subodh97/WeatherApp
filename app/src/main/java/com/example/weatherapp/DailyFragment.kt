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

class DailyFragment : Fragment() {
    private var columnCount = 1
    private var lat = 0.0
    private var long = 0.0
    lateinit var rView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
           lat = it.getDouble(ARG_LAT)
            long = it.getDouble(ARG_LONG)

        }
        val key = resources.getString(R.string.appid)
        val request =
            WeatherDataInterface.getInstance().getDailyWeather(lat, long,
                    "minutely,current,hourly", "metric", key)
        request.enqueue(WeatherCallback())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily, container, false)
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
        rView = view.findViewById(R.id.rViewDaily)

        super.onViewCreated(view, savedInstanceState)
    }
    companion object {

        // TODO: Rename and change types and number of parameters

        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_LAT = "lat"
        const val ARG_LONG = "long"

        @JvmStatic
        fun newInstance(columnCount: Int, lat:Double, long:Double) =
            DailyFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putDouble(ARG_LAT,lat)
                    putDouble(ARG_LONG,long)
                }
            }
    }

    inner class WeatherCallback: Callback<DailyWeatherDetails> {
        override fun onResponse(call: Call<DailyWeatherDetails>,
                                response: Response<DailyWeatherDetails>) {

            if(response.isSuccessful){
                val weatherDetails =  response.body()
                Log.d("DailyFragment","List:$weatherDetails")

                weatherDetails?.daily?.let{
                    rView.adapter = DailyWeatherAdapter(it)

                }

            }
            else{
                Log.d("DailyFragment","$response")
            }

        }

        override fun onFailure(call: Call<DailyWeatherDetails>, t: Throwable) {
            Toast.makeText(context,
                "failed to get weather: ${t.message}", Toast.LENGTH_LONG).show()
            Log.d("DailyFragment","error: ${t.message}")
        }
    }

}