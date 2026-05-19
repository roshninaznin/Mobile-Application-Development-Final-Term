package com.example.labtask_12_universitystudentportal

import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var etEmail: TextInputEditText
    lateinit var btnResetPassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        auth = FirebaseAuth.getInstance()
        etEmail = findViewById(R.id.etEmail)
        btnResetPassword = findViewById(R.id.btnResetPassword)
        btnResetPassword.setOnClickListener {
            sendResetEmail()
        }
    }

    fun sendResetEmail() {
        val email = etEmail.text.toString().trim()
        when{
            email.isEmpty() -> {
                etEmail.error = "Email is required"
                etEmail.requestFocus()
                return
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                etEmail.error = "Please enter a valid email"
                etEmail.requestFocus()
                return
            }
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Reset password link sent to your email. Check you inbox", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}