package com.example.labtask_13_studentcoursemanager

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var recyclerCourses: RecyclerView
    lateinit var searchView: SearchView
    lateinit var emptyLayout: LinearLayout
    lateinit var fabAddCourse: FloatingActionButton
    lateinit var database: DatabaseReference
    lateinit var adapter: CourseAdapter

    val courseList= ArrayList<Course>()
    val filteredList = ArrayList<Course>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerCourses = findViewById(R.id.recyclerCourses)
        searchView = findViewById(R.id.searchView)
        emptyLayout = findViewById(R.id.emptyLayout)
        fabAddCourse = findViewById(R.id.fabAddCourse)
        adapter = CourseAdapter(this, courseList)
        recyclerCourses.layoutManager= LinearLayoutManager(this)
        adapter = CourseAdapter(this, filteredList)
        recyclerCourses.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("courses")
        loadCourses()

        fabAddCourse.setOnClickListener {
            startActivity(Intent(this, AddCourseActivity::class.java))
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterCourses(newText.toString())
                return true
            }
        })
    }

    fun loadCourses() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                courseList.clear()
                for (courseSnapshot in snapshot.children) {
                    val course = courseSnapshot.getValue(Course::class.java)
                    if (course != null) {
                        courseList.add(course)
                    }
                }

                filteredList.clear()
                filteredList.addAll(courseList)
                adapter.notifyDataSetChanged()

                if(courseList.isEmpty()){
                    emptyLayout.visibility = android.view.View.VISIBLE
                }else{
                    emptyLayout.visibility = android.view.View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    fun filterCourses(query: String) {
        val tempList = ArrayList<Course>()
        for (course in courseList) {
            if (course.courseName.lowercase().contains(query.lowercase()) || course.courseCode.lowercase().contains(query.lowercase()) ){
                tempList.add(course)
            }
        }

        adapter.updateList(tempList)

    }


}