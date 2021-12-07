package com.rapidops.salesmatechat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rapidops.salesmatechat.databinding.ActivityMainBinding
import com.rapidops.salesmatechatsdk.core.SalesmateChatSDK

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SalesmateChatSDK.getInstance().logDebug("Print log")

        binding.btnStartChat.setOnClickListener {
            SalesmateChatSDK.getInstance().startMessenger()
        }


        binding.btnSendEvent.setOnClickListener {
            val eventName = binding.edtEventName.text.toString().trim()
            val key = binding.edtKey.text.toString().trim()
            val value = binding.edtValue.text.toString().trim()
            val data = hashMapOf<String, String>()
            data[key] = value
            SalesmateChatSDK.getInstance().recordEvent(eventName, data)
        }

    }
}