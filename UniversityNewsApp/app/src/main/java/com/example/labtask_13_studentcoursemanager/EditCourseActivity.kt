package com.example.labtask_13_studentcoursemanager

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class EditCourseActivity : AppCompatActivity() {
    lateinit var etCourseName: EditText
    lateinit var etCourseCode: EditText
    lateinit var etInstructor: EditText
    lateinit var spCredit: Spinner
    lateinit var etRoom: EditText
    lateinit var etSchedule: EditText
    lateinit var spSemester: Spinner

    lateinit var progressBar: ProgressBar
    lateinit var btnCancel: Button
    lateinit var btnUpdateCourse: Button
    lateinit var btnDeleteCourse: Button
    lateinit var db: FirebaseDatabase

    var courseId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_course)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseDatabase.getInstance()

        initViews()
        setupSpinners()
        loadCourseData()

        btnUpdateCourse.setOnClickListener {
            updateCourse()
        }

        btnDeleteCourse.setOnClickListener {
            deleteCourse()
        }

        btnCancel.setOnClickListener {
            finish()

        }
    }

    fun initViews() {
        etCourseName = findViewById(R.id.etCourseName)
        etCourseCode = findViewById(R.id.etCourseCode)
        etInstructor = findViewById(R.id.etInstructor)
        spCredit = findViewById(R.id.spCredit)
        etRoom = findViewById(R.id.etRoom)
        etSchedule = findViewById(R.id.etSchedule)
        spSemester = findViewById(R.id.spSemester)
        progressBar = findViewById(R.id.progressBar)
        btnCancel = findViewById(R.id.btnCancel)
        btnUpdateCourse = findViewById(R.id.btnUpdateCourse)
        btnDeleteCourse = findViewById(R.id.btnDeleteCourse)

    }

    fun setupSpinners(){
        val credits = arrayOf("1", "2", "3")
        val semesters = arrayOf("Spring", "Summer", "Fall")

        spCredit.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, credits)
        spSemester.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, semesters)

    }

    fun loadCourseData() {
        courseId = intent.getStringExtra("courseId")?:""
        etCourseName.setText(intent.getStringExtra("courseName"))
        etCourseCode.setText(intent.getStringExtra("courseCode"))
        etInstructor.setText(intent.getStringExtra("instructor"))
        etRoom.setText(intent.getStringExtra("room"))
        etSchedule.setText(intent.getStringExtra("schedule"))

        val credit = intent.getStringExtra("credit")
        val semester = intent.getStringExtra("semester")
        val creditPosition = (spCredit.adapter as ArrayAdapter<String>).getPosition(credit)
        val semesterPosition = (spSemester.adapter as ArrayAdapter<String>).getPosition(semester)

        spCredit.setSelection(creditPosition)
        spSemester.setSelection(semesterPosition)

    }

    fun updateCourse() {

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

        val updatedCourse = Course(courseId, courseName, courseCode, instructor, credit, schedule, room, semester)

        db.getReference("courses").child(courseId).setValue(updatedCourse)
            .addOnCompleteListener {
                progressBar.visibility = ProgressBar.GONE
                Toast.makeText(this, "Course updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                progressBar.visibility = ProgressBar.GONE
                Toast.makeText(this, "Failed to update course", Toast.LENGTH_SHORT).show()

            }
    }

    fun deleteCourse() {
        progressBar.visibility = ProgressBar.VISIBLE
        db.getReference("courses").child(courseId).removeValue()
            .addOnSuccessListener {
                progressBar.visibility = ProgressBar.GONE
                Toast.makeText(this, "Course deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                progressBar.visibility = ProgressBar.GONE
                Toast.makeText(this, "Failed to delete course", Toast.LENGTH_SHORT).show()
            }

    }
}