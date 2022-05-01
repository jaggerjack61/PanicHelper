package com.example.panichelper

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.panichelper.databinding.ActivityMainBinding
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //var mapItem:DataMapItem
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
    private fun show(ti:String){
        Toast.makeText(this,ti,Toast.LENGTH_LONG).show()
    }
}