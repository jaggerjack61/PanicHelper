package com.example.measuredata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash_screen)

        val textView=findViewById<TextView>(R.id.textView)
        val imageView=findViewById<ImageView>(R.id.imageView)
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy")
        val formatted = current.format(formatter)

        textView.text="Copyright "+formatted
        imageView.alpha=0f
        imageView.animate().setDuration(1500).alpha(1f).withEndAction {
            val i= Intent(this,MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }


    }
}