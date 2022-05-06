package com.example.measuredata

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import java.nio.charset.Charset


var counter=0
var xy=""
var msg=JSONArray()

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
        msg = JSONArray(sharedPreferences.getString("JSON", null).toString())
        counter=msg.length()
        val listView = findViewById<ListView>(R.id.list_view)
//        val redColor = Color.parseColor("#FF0000")
//        listView.setBackgroundColor(redColor)
        //getVolley()
       // jsonParse()
        Log.d("Xanakin data","test")
        var sendBtn = findViewById<Button>(R.id.button6)
        sendBtn.setOnClickListener {
            postVolley()
        }

        listView.adapter = MyCustomAdapter(this) // this needs to be my custom adapter telling my list what to render



    }

    private fun getVolley(){
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
        var url = sharedPreferences.getString("email", null).toString()

        val patient_id = sharedPreferences.getString("doctorID", null).toString()
        url=url+"/api/patient/"+patient_id
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                xy=response.toString()
                showToast(response.toString())
                //textView.text = "Response: %s".format(response.toString())
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

// Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun showToast(x:String){
        Toast.makeText(this, x,Toast.LENGTH_LONG).show()
    }

    private fun jsonParse() {
        Log.d("Xanakin data","test2")
        var requestQueue = Volley.newRequestQueue(this)
//        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
//        var url = sharedPreferences.getString("email", null).toString()
//
//        val patient_id = sharedPreferences.getString("doctorID", null).toString()
//        url=url+"/api/patient/"+patient_id
        val url="https://c40c-41-174-78-248.ngrok.io/api/chat/get/1"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                // response
                val strResp = response.toString()
                val arr= JSONArray(strResp)
                msg=arr
                for(i in 0 until arr.length()){
                    val message = arr.getJSONObject(i)
                    Log.d("xana",arr.getJSONObject(i).getString("message"))
                    Log.d("Xanakin data1", message.getString("message"))


                }
                Log.d("Xanakin data1", response.toString())
                // Log.d("API", strResp)
                //Toast.makeText(this, strResp, Toast.LENGTH_LONG).show()
                //days.text="Last panic attack:"+strResp
            },
            Response.ErrorListener { error ->
                //Log.d("API", "error => $error")
                Toast.makeText(this, "failed "+error, Toast.LENGTH_LONG).show()
            })

        requestQueue.add(stringRequest)


    }

    private fun postVolley(){
        var message=findViewById<TextView>(R.id.editTextTextPersonName5)

            val queue = Volley.newRequestQueue(this)
            //val url = "https://private-4c0e8-simplestapi3.apiary-mock.com/message"
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
        var url = sharedPreferences.getString("email", null).toString()
//
        val patient_id = sharedPreferences.getString("doctorID", null).toString()
            url=url+"/api/chat/send/"
            val requestBody = "patient_id="+patient_id+"&message="+message.text
            val stringReq : StringRequest =
                object : StringRequest(
                    Method.POST, url,
                    Response.Listener { response ->
                        // response
                        val strResp = response.toString()
                        // Log.d("API", strResp)
                        Toast.makeText(this, "sent", Toast.LENGTH_LONG).show()
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


}

private class MyCustomAdapter(context: Context): BaseAdapter() {

    private val mContext: Context

    init {
        mContext = context
    }

    // responsible for how many rows in my list
    override fun getCount(): Int {
        return counter
    }

    // you can also ignore this
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // you can ignore this for now
    override fun getItem(position: Int): Any {
        return "TEST STRING"
    }

    // responsible for rendering out each row
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        Log.d("xana111",msg.getJSONObject(position).getString("doctor_id"))
        val textView = TextView(mContext)
        if(msg.getJSONObject(position).getString("doctor_id")!="x"){
            textView.text = msg.getJSONObject(position).getString("message")+" -from Doctor"
        }
        else{
            textView.text = msg.getJSONObject(position).getString("message")+" -from me"
        }


        //counter++
        return textView
    }

}


