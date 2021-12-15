package com.crystal.pushnotificationreceiverapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private val resultTextView:TextView by lazy{
        findViewById(R.id.resultTextView)
    }
    private val firebaseTokenTextView:TextView by lazy{
        findViewById(R.id.firebaseTokenTextView)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFirebase()
        updateResult(false,intent)
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                firebaseTokenTextView.text = task.result
            })
    }
    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent: Boolean = false, intent: Intent?){
        resultTextView.text =  (intent?.getStringExtra("notificationType") ?: "앱런처") +
        if(isNewIntent){
            "(으)로 갱신했습니다."
        }else{
            "(으)로 실행했습니다."
        }
    }

    //Single top 으로 실행하면 onNewIntent 함수가 호출됨
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        updateResult(true, intent)
    }
}