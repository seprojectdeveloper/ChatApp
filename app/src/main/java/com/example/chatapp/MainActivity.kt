package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.VerificationActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Replace with your main activity layout

        // Set up a delay before transitioning to the VerifyActivity
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, VerificationActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the main activity so the user can't navigate back to it
        }, 3000) // 3000 milliseconds = 3 seconds
    }
}
