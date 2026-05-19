package com.example.labtask_11_usersettingsmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    lateinit var etStudentName: EditText
    lateinit var rbLight: RadioButton
    lateinit var rbDark: RadioButton
    lateinit var rbSystem: RadioButton
    lateinit var switchNotification: SwitchCompat
    lateinit var spinnerLanguage: Spinner
    lateinit var seekBarFont: SeekBar
    lateinit var txtFontSize: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etStudentName = findViewById(R.id.txtStudentName)
        rbLight = findViewById(R.id.rbLight)
        rbDark = findViewById(R.id.rbDark)
        rbSystem = findViewById(R.id.rbSystem)
        switchNotification = findViewById(R.id.switchNotification)
        spinnerLanguage = findViewById(R.id.spinnerLanguage)
        seekBarFont = findViewById(R.id.seekBarFont)
        txtFontSize = findViewById(R.id.txtFontSize)

        val btnSave: Button = findViewById(R.id.btnSave)
        val btnReset: Button = findViewById(R.id.btnReset)
        val btnView: Button = findViewById(R.id.btnView)
        val fabProfile: FloatingActionButton = findViewById(R.id.fabProfile)

        val languageAdapter = ArrayAdapter.createFromResource(this,R.array.languages,android.R.layout.simple_spinner_item)
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = languageAdapter

        seekBarFont.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                txtFontSize.text = "Font Size: ${progress + 12}sp"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnSave.setOnClickListener {
            saveSettings()
        }

        btnReset.setOnClickListener {
            resetPreference()
        }

        btnView.setOnClickListener {
            startActivity(Intent(this, SettingsViewerActivity::class.java))
        }

        fabProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadSettings()
    }

    private fun saveSettings(){
        val prefs= getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val selectedTheme = when {
            rbLight.isChecked-> "light"
            rbDark.isChecked-> "dark"
            else -> "system"

        }

        with(prefs.edit()){
            putString("KEY_THEME", selectedTheme)
            putBoolean("KEY_NOTIFICATIONS", switchNotification.isChecked)
            putString("KEY_LANGUAGE", spinnerLanguage.selectedItem.toString())
            putInt("KEY_FONT_SIZE", seekBarFont.progress+12)
            putLong("KEY_LAST_SAVED", System.currentTimeMillis())
            apply()
        }

        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadSettings(){
        val prefs= getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        etStudentName.setText(prefs.getString("KEY_STUDENT_NAME", ""))

        when(prefs.getString("KEY_THEME", "light")){
            "light" -> rbLight.isChecked = true
            "dark" -> rbDark.isChecked = true
            else -> rbSystem.isChecked = true
        }

        switchNotification.isChecked = prefs.getBoolean("KEY_NOTIFICATIONS", true)
        val language = prefs.getString("KEY_LANGUAGE", "English")
        val position = (spinnerLanguage.adapter as ArrayAdapter<String>).getPosition(language)
        spinnerLanguage.setSelection(position)

        val fontSize = prefs.getInt("KEY_FONT_SIZE", 16)
        seekBarFont.progress = fontSize - 12
        txtFontSize.text = "Font Size: $fontSize sp"

    }

    private fun resetPreference() {
        val prefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        prefs.edit().clear().apply()
        rbLight.isChecked = true
        switchNotification.isChecked = true
        spinnerLanguage.setSelection(0)
        seekBarFont.progress = 4
        txtFontSize.text = "Font Size: 16sp"

        Toast.makeText(this, "Settings reset to default", Toast.LENGTH_SHORT).show()
    }
}