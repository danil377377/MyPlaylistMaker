package com.example.myplaylistmaker.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.myplaylistmaker.App
import com.example.myplaylistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial
const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"
const val EDIT_SWITCH_KEY = "key_for_edit_text"

class SettingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val app = applicationContext as App
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = app.sharedPrefs.getTheme()
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (app).switchTheme(checked)

        }

        val contractButton = findViewById<FrameLayout>(R.id.contract)
        contractButton.setOnClickListener{
            val displayIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.contract_link)))
            startActivity(displayIntent)
        }
        val backButton = findViewById<ImageView>(R.id.back_button)


        backButton.setOnClickListener{

            onBackPressed()
        }


        val supportButton = findViewById<FrameLayout>(R.id.write_to_support)
        supportButton.setOnClickListener {
            val subjectMessage  = getString(R.string.subject_message)
            val message = getString(R.string.message)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subjectMessage)
            startActivity(shareIntent)
        }
        val shareButton = findViewById<FrameLayout>(R.id.share_button)
        shareButton.setOnClickListener {

            val message = getString(R.string.share_link)
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(shareIntent)
        }
    }
}