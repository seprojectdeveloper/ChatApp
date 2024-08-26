package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.VerificationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class CodeVerificationActivity : AppCompatActivity() {

    private lateinit var verificationId: String
    private lateinit var code1: EditText
    private lateinit var code2: EditText
    private lateinit var code3: EditText
    private lateinit var code4: EditText
    private lateinit var code5: EditText
    private lateinit var code6: EditText
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.codeverify)

        verificationId = intent.getStringExtra("verificationId") ?: ""
        val userPhoneNumber = intent.getStringExtra("userPhoneNumber") ?: ""

        // Initialize EditText views
        code1 = findViewById(R.id.code1)
        code2 = findViewById(R.id.code2)
        code3 = findViewById(R.id.code3)
        code4 = findViewById(R.id.code4)
        code5 = findViewById(R.id.code5)
        code6 = findViewById(R.id.code6)
        continueButton = findViewById(R.id.continue_button)

        val verifyText: TextView = findViewById(R.id.verifytext2)
        verifyText.text = String.format(
            getString(R.string.verifyt, userPhoneNumber)
        )
        configureDigitInputs()

        continueButton.setOnClickListener {
            val enteredCode = gatherCode()
            if (enteredCode.length == 6) {
                validateCode(enteredCode)
            } else {
                Toast.makeText(this, "Please enter all digits of the code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configureDigitInputs() {
        val digitFields = listOf(code1, code2, code3, code4, code5, code6)
        for (index in digitFields.indices) {
            digitFields[index].addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.length == 1 && index < digitFields.size - 1) {
                        digitFields[index + 1].requestFocus()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun gatherCode(): String {
        return code1.text.toString() + code2.text.toString() +
                code3.text.toString() + code4.text.toString() +
                code5.text.toString() + code6.text.toString()
    }

    private fun validateCode(code: String) {
        val authCredential = PhoneAuthProvider.getCredential(verificationId, code)
        FirebaseAuth.getInstance().signInWithCredential(authCredential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Code validated successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, NameScreenActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Code validation failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
