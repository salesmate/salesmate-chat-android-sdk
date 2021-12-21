package com.rapidops.salesmatechat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rapidops.salesmatechat.databinding.ActivityMainBinding
import com.rapidops.salesmatechatsdk.core.SalesmateChatSDK
import com.rapidops.salesmatechatsdk.core.UserDetails

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


        binding.btnLogin.setOnClickListener {
            val userDetail = UserDetails.create()
            val email = binding.edtEmail.text.toString().trim()
            val firstName = binding.edtFirstName.text.toString().trim()
            val lastName = binding.edtLastName.text.toString().trim()
            val userId = binding.edtUserId.text.toString().trim()
            userDetail.withEmail(email).withFirstName(firstName).withLastName(lastName)
            SalesmateChatSDK.getInstance().login(userId, userDetail)
        }

        binding.btnUpdate.setOnClickListener {
            val userDetail = UserDetails.create()
            val email = binding.edtEmail.text.toString().trim()
            val firstName = binding.edtFirstName.text.toString().trim()
            val lastName = binding.edtLastName.text.toString().trim()
            val userId = binding.edtUserId.text.toString().trim()
            userDetail.withEmail(email)
                .withFirstName(firstName)
                .withLastName(lastName)
            SalesmateChatSDK.getInstance().update(userId, userDetail)
        }

        binding.btnLogout.setOnClickListener {
            SalesmateChatSDK.getInstance().logout()
        }

        binding.btnGetVisitorId.setOnClickListener {
            binding.txtGetVisitorId.text = SalesmateChatSDK.getInstance().getVisitorId()
        }
    }
}