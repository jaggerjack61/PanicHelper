package com.example.panichelper

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.gsm.SmsManager
import android.Manifest;
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var code=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("tst","test")

        getSMSPermissions()

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        }
        catch(e: Exception) {
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }


            getCurrentLocation()

        val getHelpBtn=findViewById<Button>(R.id.button)
        getHelpBtn.setOnClickListener {
            getHelp()


        }



        val settingsBtn=findViewById<Button>(R.id.button3)
        settingsBtn.setOnClickListener {
            val intent= Intent(this,SettingsActivity::class.java)
            startActivity(intent)
        }

        val wellnessBtn=findViewById<Button>(R.id.button5)
        wellnessBtn.setOnClickListener {
            val intent= Intent(this,WellnessActivity::class.java)
            startActivity(intent)
        }

        val exitBtn=findViewById<Button>(R.id.button2)
        exitBtn.setOnClickListener {
            finishAffinity()
        }


    }




    private fun getSMSPermissions(){
        code=101
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.SEND_SMS)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Internet permission denied",Toast.LENGTH_LONG).show()
            makeSMSRequest()
        }
    }

    private fun makeSMSRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.SEND_SMS),
            code)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                             permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            code -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
                }
            }
        }
    }








    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        try {
            // checking location permission
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//
//                // request permission
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
//                );
//
//                return
//            }
//
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return
//            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // getting the last known or current location
                    latitude = location.latitude
                    longitude = location.longitude
                    val sharedPreferences =
                        getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.apply {
                        putString("longitude", "Longitude: ${location.longitude}")
                        putString("latitude", "Latitude: ${location.latitude}")

                    }.apply()

                    // Toast.makeText(this,"Longitude: ${location.longitude}",Toast.LENGTH_LONG).show()


                }
                .addOnFailureListener {
                    Toast.makeText(
                        this, "Failed on getting current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        catch (e: Exception) {
            Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun getHelp(){
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
        val phoneNumber = sharedPreferences.getString("phoneNumber",null)
        val customText = sharedPreferences.getString("customText",null)
        val longitude = sharedPreferences.getString("longitude",null)
        val latitude = sharedPreferences.getString("latitude",null)
        if(phoneNumber != null){
            if(customText != null){
                try {
                    val smsManager = SmsManager.getDefault() as SmsManager
                    smsManager.sendTextMessage(phoneNumber, null, customText+" Lat:"+latitude+" Long:"+longitude, null, null)
                    Toast.makeText(this,"Help message sent.",Toast.LENGTH_LONG).show()
                }
                catch(e: Exception) {
                    Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
                }
            }
            else{
                try {
                    val smsManager = SmsManager.getDefault() as SmsManager
                    smsManager.sendTextMessage(phoneNumber, null, "i am having a panic attack at location Lat"+latitude+" Long:"+longitude, null, null)
                    Toast.makeText(this,"Help message sent.",Toast.LENGTH_LONG).show()
                }
                catch(e: Exception) {
                    Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
                }

            }
        }
        else{
            Toast.makeText(this,"Set a phone number please", Toast.LENGTH_LONG).show()
        }


        


    }


}