package com.rapidops.salesmatechat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rapidops.salesmatechatsdk.SalesMateChatSDK

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SalesMateChatSDK.getInstance().logDebug("Print log")
    }
}