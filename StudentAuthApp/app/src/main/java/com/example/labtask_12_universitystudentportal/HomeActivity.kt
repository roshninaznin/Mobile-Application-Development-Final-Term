package com.example.labtask_12_universitystudentportal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var txtProfileInitial: TextView
    lateinit var txtStudentEmail: TextView
    lateinit var txtStudentId: TextView
    lateinit var txtCreatedDate: TextView
    lateinit var etNewPassword: TextInputEditText
    lateinit var etConfirmPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        auth = FirebaseAuth.getInstance()
        txtProfileInitial = findViewById(R.id.txtProfileInitial)
        txtStudentEmail = findViewById(R.id.txtStudentEmail)
        txtStudentId = findViewById(R.id.txtStudentId)
        txtCreatedDate = findViewById(R.id.txtCreatedDate)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnDeleteAccount = findViewById<Button>(R.id.btnDeleteAccount)
        val btnUpdatePassword = findViewById<Button>(R.id.btnUpdatePassword)

        loadUserData()

        btnUpdatePassword.setOnClickListener {
            updatePassword()
        }

        btnLogout.setOnClickListener {
            logoutUser()
        }

        btnDeleteAccount.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun loadUserData() {

        val user = auth.currentUser

        if (user == null) {

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val email = user.email ?: "No Email"
        val uid = user.uid.take(8)

        txtStudentEmail.text = "Email: $email"
        txtStudentId.text = "Student ID: $uid"
        txtProfileInitial.text = email.firstOrNull()?.uppercaseChar()?.toString()?:"U"

        val creationTime = user.metadata?.creationTimestamp ?: 0L

        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        txtCreatedDate.text = "Created: ${sdf.format(Date(creationTime))}"
    }

    private fun updatePassword() {

        val newPassword = etNewPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        when {
            newPassword.isEmpty() -> {
                etNewPassword.error = "Enter new password"
                etNewPassword.requestFocus()
                return
            }

            newPassword.length < 8 -> {
                etNewPassword.error = "Password must be at least 8 characters"
                etNewPassword.requestFocus()
                return
            }

            newPassword != confirmPassword -> {
                etConfirmPassword.error =
                    "Passwords do not match"
                return
            }
        }

        auth.currentUser
            ?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    etNewPassword.setText("")
                    etConfirmPassword.setText("")

                } else {
                    Snackbar.make(findViewById(android.R.id.content), task.exception?.message ?: "Failed to update password", Snackbar.LENGTH_LONG).show()
                }
            }
    }

    private fun logoutUser() {

        auth.signOut()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showDeleteConfirmation() {

        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to permanently delete your account?")
            .setPositiveButton("Delete") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteAccount() {

        auth.currentUser?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, RegisterActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()

                } else {

                    Snackbar.make(findViewById(android.R.id.content), task.exception?.message
                            ?: "Failed to delete account", Snackbar.LENGTH_LONG).show()
                }
            }
    }
}