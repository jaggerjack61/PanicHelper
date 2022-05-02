package com.example.measuredata

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.nio.charset.Charset

class SettingsActivity : AppCompatActivity() {

    var code=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        loadSettings()
        getLocationPermissions()
        val saveSettingsBtn=findViewById<Button>(R.id.button)
        saveSettingsBtn.setOnClickListener {
            saveSettings()
        }



    }
    private fun saveSettings(){
        val phoneNumber=findViewById<EditText>(R.id.editTxt).text.toString()
        val url=findViewById<EditText>(R.id.editTxt2).text.toString()
        val message=findViewById<EditText>(R.id.editTxt3).text.toString()
        val patient_id=findViewById<EditText>(R.id.editTxt4).text.toString()
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.apply{
            putString("phoneNumber",phoneNumber)
            putString("email",url)
            putString("customText",message)
            putString("doctorID",patient_id)

        }.apply()

        postVolley(url, patient_id, message, phoneNumber)


        Toast.makeText(this,"Settings have been saved",Toast.LENGTH_SHORT).show()


    }

    private fun loadSettings() {
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
        val phoneNumber = sharedPreferences.getString("phoneNumber", null)
        val url = sharedPreferences.getString("email", null)
        val message = sharedPreferences.getString("customText", null)
        val patient_id = sharedPreferences.getString("doctorID", null)
        val phoneNumberTxt = findViewById<EditText>(R.id.editTxt)
        val emailTxt = findViewById<EditText>(R.id.editTxt2)
        val customTextTxt = findViewById<EditText>(R.id.editTxt3)
        val doctorIDTxt = findViewById<EditText>(R.id.editTxt4)
        phoneNumberTxt.setText(phoneNumber)
        emailTxt.setText(url)
        customTextTxt.setText(message)
        doctorIDTxt.setText(patient_id)

    }


    private fun postVolley(url:String,patient_id:String,message:String,phoneNumber:String) {
        val queue = Volley.newRequestQueue(this)
        //val url = "https://private-4c0e8-simplestapi3.apiary-mock.com/message"
        var url=url
        url=url+"/api/settings/"
        val requestBody = "patient_id="+patient_id+"&url="+url+"&message="+message+"&phone_no="+phoneNumber
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


    private fun getLocationPermissions(){
        code=102
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Internet permission denied",Toast.LENGTH_LONG).show()
            makeLocationRequest()
        }
    }

    private fun makeLocationRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
}