package com.example.firebasecrashlytics

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.firebasecrashlytics.Notification.FirebaseMessagingService
import com.example.firebasecrashlytics.Notification.NotificationData
import com.example.firebasecrashlytics.Notification.PushNotification
import com.example.firebasecrashlytics.Notification.RetrofitService
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_cloud_messaging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CloudMessaging : AppCompatActivity() {
    val TOPIC = "/topics/myTopic2"

    val TAG = "CloudMessaging"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_messaging)
        FirebaseMessagingService.sharedPref =
            getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseMessagingService.token = it.token
            etToken.setText(it.token)
            Log.d(TAG, "onCreate: " + it.token)
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        btnSend.setOnClickListener {
            val title = etTitle.text.toString()
            val message = etMessage.text.toString()
            val recipientToken = etToken.text.toString()
            if (title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                PushNotification(
                    NotificationData(title, message),
                    recipientToken
                ).also {
                    sendNotification(it)
                }
            }
        }
    }

    private fun sendNotification(notification: PushNotification)  = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitService.getService().postNotification(notification)
            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}