package com.example.labtask_11_usersettingsmanager

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Locale

class SettingsViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings_viewer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val txtTheme: TextView =findViewById(R.id.txtSavedTheme)
        val txtNotification: TextView =findViewById(R.id.txtSavedNotification)
        val txtLanguage: TextView =findViewById(R.id.txtSavedLanguage)
        val txtFontSize: TextView =findViewById(R.id.txtSavedFontSize)
        val txtLastSaved: TextView =findViewById(R.id.txtLastSaved)
        val txtNoData: TextView =findViewById(R.id.txtNoData)
        val btnEdit: Button =findViewById(R.id.btnEdit)

        val prefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val lastSaved = prefs.getLong("KEY_LAST_SAVED", 0)
        if(lastSaved == 0L){
            txtNoData.visibility = TextView.VISIBLE
        }else{
            txtTheme.text=prefs.getString("KEY_THEME","Light")
            txtNotification.text=
                if(prefs.getBoolean("KEY_NOTIFICATIONS",true)) "Enabled"
                else "Disabled"
            txtLanguage.text=prefs.getString("KEY_LANGUAGE","English")
            txtFontSize.text="${prefs.getInt("KEY_FONT_SIZE",16)}sp"
            val sdf= SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault())
            txtLastSaved.text="Last Saved: ${sdf.format(lastSaved)}"
        }

        btnEdit.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}