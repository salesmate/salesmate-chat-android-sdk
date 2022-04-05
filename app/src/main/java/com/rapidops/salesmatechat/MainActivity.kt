package com.rapidops.salesmatechat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.rapidops.salesmatechat.databinding.ActivityMainBinding
import com.rapidops.salesmatechatsdk.app.interfaces.LoginListener
import com.rapidops.salesmatechatsdk.app.interfaces.UpdateListener
import com.rapidops.salesmatechatsdk.core.SalesmateChatSDK
import com.rapidops.salesmatechatsdk.core.UserDetails
import com.rapidops.salesmatechatsdk.domain.exception.SalesmateException

class MainActivity : AppCompatActivity() {


    companion object {
        private val TAG = MainActivity::class.java.name
    }

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
            SalesmateChatSDK.getInstance().login(userId, userDetail, object : LoginListener {
                override fun onLogin() {
                    binding.txtLoginUpdateStatus.text = "Login successfully"
                }

                override fun onError(salesmateException: SalesmateException) {
                    binding.txtLoginUpdateStatus.text = salesmateException.errorMessage
                    salesmateException.printStackTrace()
                }
            })
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
            SalesmateChatSDK.getInstance().update(userId, userDetail, object : UpdateListener {
                override fun onUpdate() {
                    binding.txtLoginUpdateStatus.text = "Update successfully"
                }

                override fun onError(salesmateException: SalesmateException) {
                    binding.txtLoginUpdateStatus.text = salesmateException.errorMessage
                    salesmateException.printStackTrace()
                }
            })
        }

        binding.btnLogout.setOnClickListener {
            SalesmateChatSDK.getInstance().logout()
        }

        binding.btnGetVisitorId.setOnClickListener {
            binding.txtGetVisitorId.text = SalesmateChatSDK.getInstance().getVisitorId()
        }

        binding.btnGenerateFirebaseToken.setOnClickListener {
            generateFirebaseToken()
        }
    }

    private fun generateFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d(TAG, "Firebase message token:- $token")
            token?.let {

            }
        })
    }
}