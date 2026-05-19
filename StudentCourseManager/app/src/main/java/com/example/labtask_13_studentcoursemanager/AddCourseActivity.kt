package com.example.labtask_13_studentcoursemanager

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class AddCourseActivity : AppCompatActivity() {
    lateinit var etCourseName: EditText
    lateinit var etCourseCode: EditText
    lateinit var etInstructor: EditText
    lateinit var spCredit: Spinner
    lateinit var etRoom: EditText
    lateinit var etSchedule: EditText
    lateinit var spSemester: Spinner
    lateinit var progressBar: ProgressBar
    lateinit var btnSave: Button
    lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_course)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etCourseName = findViewById(R.id.etCourseName)
        etCourseCode = findViewById(R.id.etCourseCode)
        etInstructor = findViewById(R.id.etInstructor)
        spCredit = findViewById(R.id.spCredit)
        etRoom = findViewById(R.id.etRoom)
        etSchedule = findViewById(R.id.etSchedule)
        spSemester = findViewById(R.id.spSemester)
        progressBar = findViewById(R.id.progressBar)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        setupSpinners()

        btnSave.setOnClickListener {
            saveCourse()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    fun setupSpinners(){
        val credits = arrayOf("1", "2", "3")
        val semesters = arrayOf("Spring", "Summer", "Fall")

        spCredit.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, credits)
        spSemester.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, semesters)

    }

    fun saveCourse() {
        val courseName = etCourseName.text.toString().trim()
        val courseCode = etCourseCode.text.toString().trim()
        val instructor = etInstructor.text.toString().trim()
        val credit = spCredit.selectedItem.toString()
        val room = etRoom.text.toString().trim()
        val schedule = etSchedule.text.toString().trim()
        val semester = spSemester.selectedItem.toString()

        if(courseName.isEmpty() || courseCode.isEmpty() || instructor.isEmpty() || room.isEmpty() || schedule.isEmpty()){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = ProgressBar.VISIBLE
        val database = FirebaseDatabase.getInstance().getReference("courses")
        val courseId = database.push().key!!
        val course = Course(courseId, courseName, courseCode, instructor, credit, schedule, room, semester)
        database.child(courseId).setValue(course).addOnCompleteListener {
            progressBar.visibility = ProgressBar.GONE
            Toast.makeText(this, "Course added successfully", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            progressBar.visibility = ProgressBar.GONE
            Toast.makeText(this, "Failed to add course", Toast.LENGTH_SHORT).show()
        }




    }
}