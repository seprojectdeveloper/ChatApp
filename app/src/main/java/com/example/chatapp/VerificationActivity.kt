package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.CodeVerificationActivity
import com.example.chatapp.MainActivity
import com.example.chatapp.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class VerificationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var phoneNumberEditText: EditText
    private lateinit var continueButton: Button
    private lateinit var countryCodeEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verification) // Ensure this matches the layout file name

        auth = FirebaseAuth.getInstance()
        phoneNumberEditText = findViewById(R.id.verifytext4)
        continueButton = findViewById(R.id.continue_button)
        countryCodeEditText = findViewById(R.id.verifytext3)

        continueButton.setOnClickListener {

            val countryCode = countryCodeEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()

            // Combine the country code and phone number
            val fullPhoneNumber = "$countryCode$phoneNumber"

            Log.d("verification activity", "continue button press $fullPhoneNumber")

            if (countryCode.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please enter both country code and phone number", Toast.LENGTH_SHORT).show()
            } else if (fullPhoneNumber.length < 10) {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            } else {
                startPhoneNumberVerification(fullPhoneNumber)
            }
        }

    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout duration
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto verification handled by Firebase
                    Toast.makeText(this@VerificationActivity, "Verification complete", Toast.LENGTH_SHORT).show()
                    // Optionally, auto-sign in the user
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Verification completed and user signed in
                            startActivity(Intent(this@VerificationActivity, MainActivity::class.java)) // Corrected activity name
                            finish()
                        } else {
                            Toast.makeText(this@VerificationActivity, "Verification failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@VerificationActivity, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    Toast.makeText(this@VerificationActivity, "Code sent", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@VerificationActivity, CodeVerificationActivity::class.java) // Corrected activity name
                    intent.putExtra("verificationId", verificationId)
                    intent.putExtra("phoneNumber", phoneNumber) // Pass the phone number to the next activity
                    startActivity(intent)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}
