package com.example.panichelper

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        loadSettings()
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
}