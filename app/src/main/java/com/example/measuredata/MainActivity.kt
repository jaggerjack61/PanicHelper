package com.example.measuredata

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
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import java.nio.charset.Charset
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    var url=""
    var patient_id=""
    var xyz=0


    private var code=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("tst","test")

        getSMSPermissions()
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
        val url = sharedPreferences.getString("email", null).toString()
        val patient_id = sharedPreferences.getString("doctorID", null).toString()

        try {
            getVolley(url, patient_id)
        }
        catch (e: VolleyError){
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }

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


//        val dataClient = Wearable.getDataClient(this)
//        val dataRequest = PutDataMapRequest.create("/DATA_PATH").apply {
//            dataMap.putString("KEY_STRING", "data_string")
////            dataMap.putBoolean("KEY_BOOLEAN", true)
////            dataMap.putInt("KEY_INT", 1)
//        }
//
//        val putDataRequest = dataRequest.asPutDataRequest()
//        dataClient.putDataItem(putDataRequest)
        thread(start=true,isDaemon=true){
            getData()
        }

//        Make this into a thread





    }

    private fun getData(){
        while(true) {
            val dataClient = Wearable.getDataClient(this)
            dataClient.dataItems.addOnSuccessListener { dataItems ->
                dataItems.forEach { item ->
                    if (item.uri.path == "/DATA_PATH") {
                        val mapItem = DataMapItem.fromDataItem(item)
                        mapItem.dataMap.getString("KEY_STRING")?.let { show(it) }

                        mapItem.dataMap.getString("KEY_STRING")
                        mapItem.dataMap.getBoolean("KEY_BOOLEAN")
                        mapItem.dataMap.getInt("KEY_INT")
                    }
                }
            }
        }
    }

    private fun show(ti:String){
        val text=findViewById<TextView>(R.id.textView4)
        //val switch=findViewById<Switch>(R.id.switch1)

        if(!(ti=="data_string")){
            Log.d("hbm",ti)
            if(xyz==0){
                if(ti.toDouble()>110) {
                    val text1=findViewById<TextView>(R.id.textView4)
                    text1.text="Getting assistance now!!"
                    xyz=1
                    getHelp()
                }
                if(xyz==0){
                    text.text="Heart Rate:"+ti
                }
            }
            if(ti.toDouble()<110){
                xyz=0
            }





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
        url = sharedPreferences.getString("email", null).toString()

        patient_id = sharedPreferences.getString("doctorID", null).toString()
        if(phoneNumber != null){
            if(customText != null){
                try {
                    val smsManager = SmsManager.getDefault() as SmsManager
                    smsManager.sendTextMessage(phoneNumber, null, customText+" "+latitude+" "+longitude, null, null)
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

        postVolley(url,patient_id)
        


    }

    private fun postVolley(url:String,patient_id:String) {
        val queue = Volley.newRequestQueue(this)
        //val url = "https://private-4c0e8-simplestapi3.apiary-mock.com/message"
        var url=url
        url=url+"/api/patient/panic/"
        val requestBody = "patient_id="+patient_id
        val stringReq : StringRequest =
            object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    // response
                    val strResp = response.toString()
                    // Log.d("API", strResp)
                    Toast.makeText(this, strResp, Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { error ->
                    //Log.d("API", "error => $error")
                    Toast.makeText(this, "failed "+error, Toast.LENGTH_LONG).show()
                }
            ){
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }
        queue.add(stringReq)
    }
//
    @SuppressLint("SetTextI18n")
    private fun getVolley(url:String,patient_id: String) {
    val days=findViewById<TextView>(R.id.textView3)
    val curl=url+"/api/patient/days/"+patient_id
    val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, curl,
            Response.Listener { response ->
                // response
                val strResp = response.toString()
                // Log.d("API", strResp)
                    //Toast.makeText(this, strResp, Toast.LENGTH_LONG).show()
                days.text="Last panic attack:"+strResp
            },
            Response.ErrorListener { error ->
                //Log.d("API", "error => $error")
                Toast.makeText(this, "failed "+error, Toast.LENGTH_LONG).show()
            })

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }


    }