package com.example.labtask_11_usersettingsmanager

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : AppCompatActivity() {
    lateinit var etStudentId: EditText
    lateinit var etStudentName: EditText
    lateinit var spinnerDepartment: Spinner
    lateinit var spinnerYear: Spinner
    lateinit var etEmail: EditText
    lateinit var txtWelcome: TextView
    lateinit var btnSaveProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        txtWelcome = findViewById(R.id.txtWelcome)
        etStudentId = findViewById(R.id.etStudentId)
        etStudentName = findViewById(R.id.etStudentName)
        spinnerDepartment = findViewById(R.id.spinnerDepartment)
        spinnerYear = findViewById(R.id.spinnerYear)
        etEmail = findViewById(R.id.etEmail)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)

        val departmentAdapter = ArrayAdapter.createFromResource(this,R.array.departments,android.R.layout.simple_spinner_item)
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDepartment.adapter = departmentAdapter

        val yearAdapter = ArrayAdapter.createFromResource(this,R.array.years,android.R.layout.simple_spinner_item)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYear.adapter = yearAdapter

        loadProfile()

        btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile(){
        val prefs= getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
        with(prefs.edit()){
            putString("KEY_STUDENT_ID", etStudentId.text.toString())
            putString("KEY_STUDENT_NAME", etStudentName.text.toString())
            putString("KEY_DEPARTMENT", spinnerDepartment.selectedItem.toString())
            putString("KEY_YEAR", spinnerYear.selectedItem.toString())
            putString("KEY_EMAIL", etEmail.text.toString())
            apply()
        }

        txtWelcome.text = "Welcome back, ${etStudentName.text}!"
        Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show()

    }

    private fun loadProfile(){
        val prefs= getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
        etStudentId.setText(prefs.getString("KEY_STUDENT_ID", ""))
        val name = prefs.getString("KEY_STUDENT_NAME", "")
        etStudentName.setText(name)
        txtWelcome.text = "Welcome back, $name!"
        etEmail.setText(prefs.getString("KEY_EMAIL", ""))

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}