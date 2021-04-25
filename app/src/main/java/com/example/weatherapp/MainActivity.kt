package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import java.util.concurrent.CountDownLatch

class MainActivity : AppCompatActivity(), LocationListener {
    lateinit var lManager: LocationManager
    var currentLoc: Location? = null
    var weatherShowOption:String  = "Current"   //current, daily or hourly (default= current)



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        checkPermissions()
        lManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val providerList = lManager.getProviders(true)

        var providerName = ""
        if (providerList.isNotEmpty()){

            if(providerList.contains(LocationManager.GPS_PROVIDER)){
                providerName = LocationManager.GPS_PROVIDER
            }else if (providerList.contains(LocationManager.NETWORK_PROVIDER)){
                providerName = LocationManager.NETWORK_PROVIDER
            }else{
                providerName = providerList[0]
            }
            Log.d("MainActivity", "Provider: $providerName")

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                val loc = lManager.getLastKnownLocation(providerName)
            if (loc != null) {
                currentLoc = loc
                Log.d("MainActivity", "Location: ${loc.latitude}")
            } else {
                Toast.makeText(this, "No Location found", Toast.LENGTH_LONG).show()

            }
        }

            val time:Long = 1000
            val distance: Float = 10.0f
            lManager.requestLocationUpdates(providerName, time, distance, this)
        }
        else{
            Toast.makeText(this, "Pls enable location", Toast.LENGTH_LONG).show()
        }

    }





    private fun getAddress(): String {
        val gCoder = Geocoder(this)
        currentLoc?.let{

            val addressList = gCoder.getFromLocation(
                    it.latitude,
                    it.longitude, 10)

            if (addressList.isNotEmpty()){
                val addr = addressList[0]
                var strAddress = ""
                for ( i in 0..addr.maxAddressLineIndex){
                    strAddress += addr.getAddressLine(i)
                }
                return strAddress
            }
        }

        return ""
    }

    private fun getCoordintes(location:String):Address?{
        val gCoder = Geocoder(this)
        val addressList = gCoder.getFromLocationName(location,1)
        if(addressList.isNotEmpty()){
            val addr = addressList[0]
            return addr
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions(){

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }
        else {
            Toast.makeText(this, "Location permission granted",
                    Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {

        if(grantResults.isNotEmpty()){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted",
                        Toast.LENGTH_LONG).show()
            }
            else
            {
                finish()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun buttonClick(view: View){

        val locationName = findViewById<TextView>(R.id.locationShow)
        when(view.id){
            R.id.currLocationBtn ->{
                if(getAddress().isNotEmpty()) {
                    locationName.text = getAddress()

                    when (weatherShowOption) {

                        "Daily" -> {
                            val listFrag = DailyFragment.newInstance(1, currentLoc?.latitude!!,
                                    currentLoc?.longitude!!)
                            supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.parentLyt, listFrag)
                                    .commit()
                        }
                        "Hourly" -> {
                            val listFrag = HourlyFragment.newInstance(1, currentLoc?.latitude!!,
                                    currentLoc?.longitude!!)
                            supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.parentLyt, listFrag)
                                    .commit()
                        }
                        //default = current
                        else -> {
                            val listFrag = CurrentFragment.newInstance(1, currentLoc?.latitude!!,
                                    currentLoc?.longitude!!)
                            supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.parentLyt, listFrag)
                                    .commit()
                        }

                    }
                }
                else{
                    Toast.makeText(this,"Location not found",Toast.LENGTH_LONG).show()
                }


            }
            R.id.searchBtn ->{

                val citySearch = findViewById<EditText>(R.id.citySearchE).text
                    if(citySearch.isNotEmpty() && getCoordintes(citySearch.toString()) !=null){
                        val addr = getCoordintes(citySearch.toString())
                        locationName.text = addr?.featureName


                        when(weatherShowOption){
                            "Daily" -> {
                                val listFrag = DailyFragment.newInstance(1,addr?.latitude!!,
                                        addr?.longitude!!)
                                supportFragmentManager
                                        .beginTransaction()
                                        .replace(R.id.parentLyt,listFrag)
                                        .commit()
                            }

                            "Hourly" ->{
                                val listFrag = HourlyFragment.newInstance(1,addr?.latitude!!,
                                        addr?.longitude!!)
                                supportFragmentManager
                                        .beginTransaction()
                                        .replace(R.id.parentLyt,listFrag)
                                        .commit()
                            }
                            //default = current
                            else ->{
                                val listFrag = CurrentFragment.newInstance(1,addr?.latitude!!,
                                        addr.longitude)
                                supportFragmentManager
                                        .beginTransaction()
                                        .replace(R.id.parentLyt,listFrag)
                                        .commit()
                            }
                        }

                    }
                else{
                    Toast.makeText(this,"Pls enter valid city name",Toast.LENGTH_LONG).show()
                    }

            }
        }




    }



    override fun onLocationChanged(newLoc: Location) {
                currentLoc = newLoc
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menu?.add("Current")

       menu?.add("Daily")
       menu?.add("Hourly")


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
            "Current" ->
                weatherShowOption = "Current"

            "Daily" ->
                weatherShowOption = "Daily"
            "Hourly" -> weatherShowOption = "Hourly"
        }
        return super.onOptionsItemSelected(item)
    }

}