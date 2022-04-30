package com.example.panichelper

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
        val email=findViewById<EditText>(R.id.editTxt2).text.toString()
        val customText=findViewById<EditText>(R.id.editTxt3).text.toString()
        val doctorID=findViewById<EditText>(R.id.editTxt4).text.toString()
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.apply{
            putString("phoneNumber",phoneNumber)
            putString("email",email)
            putString("customText",customText)
            putString("doctorID",doctorID)

        }.apply()

        Toast.makeText(this,"Settings have been saved",Toast.LENGTH_SHORT).show()


    }

    private fun loadSettings(){
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
        val phoneNumber=sharedPreferences.getString("phoneNumber",null)
        val email=sharedPreferences.getString("email",null)
        val customText=sharedPreferences.getString("customText",null)
        val doctorID=sharedPreferences.getString("doctorID",null)
        val phoneNumberTxt=findViewById<EditText>(R.id.editTxt)
        val emailTxt=findViewById<EditText>(R.id.editTxt2)
        val customTextTxt=findViewById<EditText>(R.id.editTxt3)
        val doctorIDTxt=findViewById<EditText>(R.id.editTxt4)
        phoneNumberTxt.setText(phoneNumber)
        emailTxt.setText(email)
        customTextTxt.setText(customText)
        doctorIDTxt.setText(doctorID)

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