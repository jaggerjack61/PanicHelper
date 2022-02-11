package com.example.panichelper

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getHelpBtn=findViewById<Button>(R.id.button)
        getHelpBtn.setOnClickListener {
            getHelp()
        }



        val settingsBtn=findViewById<Button>(R.id.button3)
        settingsBtn.setOnClickListener {
            val intent= Intent(this,SettingsActivity::class.java)
            startActivity(intent)
        }


    }
    private fun getHelp(){
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
        


    }
}