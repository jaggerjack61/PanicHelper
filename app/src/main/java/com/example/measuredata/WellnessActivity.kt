package com.example.measuredata


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.nio.charset.Charset


class WellnessActivity : AppCompatActivity() {
    var code=0
    var url=""
    var patient_id=""
    var anxiety=""
    var stress=""
    var well_being=""
    var agitation=""

//    val textView=findViewById<EditText>(R.id.editTextTextPersonName2)
//    val textView1=findViewById<EditText>(R.id.editTextTextPersonName3)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wellness)
        getInternetPermissions()
        //sendSignUpDataToServer("1")
        loadSettings()
        //postVolley()

        val anxietyField=findViewById<EditText>(R.id.editTextTextPersonName2)
        val stressField=findViewById<EditText>(R.id.editTextTextPersonName)
        val wellnessField=findViewById<EditText>(R.id.editTextTextPersonName4)
        val agitationField=findViewById<EditText>(R.id.editTextTextPersonName3)

        val submit=findViewById<Button>(R.id.button4)
        submit.setOnClickListener {
            anxiety=anxietyField.text.toString()
            stress=stressField.text.toString()
            well_being=wellnessField.text.toString()
            agitation=agitationField.text.toString()
            postVolley()
        }
    }

    private fun getInternetPermissions(){
        code=103
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.INTERNET)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Internet permission denied",Toast.LENGTH_LONG).show()
            makeInternetRequest()
        }
    }

    private fun makeInternetRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.INTERNET),
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



        //url = "https://9615-77-246-50-240.ngrok.io/api/patient/panic/"
        fun postVolley() {
            val queue = Volley.newRequestQueue(this)
            //val url = "https://private-4c0e8-simplestapi3.apiary-mock.com/message"
            url=url+"/api/patient/wb/"
            val requestBody = "patient_id="+patient_id+"&anxiety="+anxiety+"&well_being="+well_being+"&stress="+stress+"&agitation="+agitation
            val stringReq : StringRequest =
                object : StringRequest(Method.POST, url,
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


    private fun loadSettings() {
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)

         url = sharedPreferences.getString("email", null).toString()

         patient_id = sharedPreferences.getString("doctorID", null).toString()
    }




    }
