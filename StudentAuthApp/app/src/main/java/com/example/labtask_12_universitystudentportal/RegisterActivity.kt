package com.example.labtask_12_universitystudentportal

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var etFullName: TextInputEditText
    lateinit var etEmail:TextInputEditText
    lateinit var etPassword: TextInputEditText
    lateinit var etConfirmPassword: TextInputEditText
    lateinit var btnRegister: Button
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        etFullName = findViewById(R.id.txtFullName)
        etEmail = findViewById(R.id.txtEmail)
        etPassword = findViewById(R.id.txtPassword)
        etConfirmPassword = findViewById(R.id.txtConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.progressBar)

        val txtLogin = findViewById<TextView>(R.id.txtLogin)

        txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            registerUser()
        }


    }

    fun registerUser() {
        val fullName = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        when{
            fullName.isEmpty() -> {
                etFullName.error = "Full Name is required"
                etFullName.requestFocus()
                return
            }
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
            password.isEmpty() -> {
                etPassword.error = "Password is required"
                etPassword.requestFocus()
                return
            }
            password.length < 8 -> {
                etPassword.error = "Password must be at least 8 characters"
                etPassword.requestFocus()
                return
            }
            confirmPassword.isEmpty() -> {
                etConfirmPassword.error = "Confirm Password is required"
                etConfirmPassword.requestFocus()
                return
            }
            password != confirmPassword -> {
                etConfirmPassword.error = "Password does not match"
                etConfirmPassword.requestFocus()
                return
            }
        }

        progressBar.visibility = ProgressBar.VISIBLE
        btnRegister.isEnabled = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = ProgressBar.GONE
                btnRegister.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finish()
                } else {
//                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Snackbar.make(findViewById(android.R.id.content), "Registration failed: ${task.exception?.message}", Snackbar.LENGTH_LONG).show()
                }
            }

    }
}