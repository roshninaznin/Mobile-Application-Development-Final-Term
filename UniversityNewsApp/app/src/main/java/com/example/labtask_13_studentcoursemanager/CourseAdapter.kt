package com.example.labtask_13_studentcoursemanager

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class CourseAdapter(private val context: Context, private var courseList: ArrayList<Course>) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCourseName: TextView = itemView.findViewById(R.id.txtCourseName)
        val txtCourseCode: TextView = itemView.findViewById(R.id.txtCourseCode)
        val txtInstructor: TextView = itemView.findViewById(R.id.txtInstructor)
        val txtCourseCredit: TextView = itemView.findViewById(R.id.txtCourseCredit)
        val txtSchedule: TextView = itemView.findViewById(R.id.txtSchedule)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {

        val course = courseList[position]
        holder.txtCourseName.text = course.courseName
        holder.txtCourseCode.text = course.courseCode
        holder.txtInstructor.text = "Instructor:${course.instructor}"
        holder.txtCourseCredit.text = "Credit:${course.credit}"
        holder.txtSchedule.text = course.schedule
        holder.itemView.setOnClickListener {
            val intent = Intent(context, CourseDetailActivity::class.java)
            intent.putExtra("courseId", course.id)
            intent.putExtra("courseName", course.courseName)
            intent.putExtra("courseCode", course.courseCode)
            intent.putExtra("instructor", course.instructor)
            intent.putExtra("credit", course.credit)
            intent.putExtra("room", course.room)
            intent.putExtra("schedule", course.schedule)
            intent.putExtra("semester", course.semester)
            context.startActivity(intent)
        }

        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, EditCourseActivity::class.java)

            intent.putExtra("courseId", course.id)
            intent.putExtra("courseName", course.courseName)
            intent.putExtra("courseCode", course.courseCode)
            intent.putExtra("instructor", course.instructor)
            intent.putExtra("credit", course.credit)
            intent.putExtra("room", course.room)
            intent.putExtra("schedule", course.schedule)
            intent.putExtra("semester", course.semester)

            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("courses").child(course.id).removeValue()
        }
    }

    fun updateList(newList: ArrayList<Course>) {
        courseList = newList
        notifyDataSetChanged()
    }
}