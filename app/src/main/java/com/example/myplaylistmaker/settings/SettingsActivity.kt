package com.example.myplaylistmaker.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.example.imdbtraining.utility.Creator
import com.example.myplaylistmaker.App
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.settings.presentation.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        val app = applicationContext as App
        val settingsInteractor = Creator.provideSettingsInteractor(app)
        val sharingInteractor = Creator.provideSharingInteractor(this)
        val viewModel = SettingsViewModel(sharingInteractor, settingsInteractor)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = viewModel.getThemeSettings()
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.updateThemeSettings(checked)

        }

        val contractButton = findViewById<FrameLayout>(R.id.contract)
        contractButton.setOnClickListener{
           viewModel.openTherms()
        }
        val shareButton = findViewById<FrameLayout>(R.id.share_button)
        shareButton.setOnClickListener {

            viewModel.shareApp()
        }
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener{

            onBackPressed()
        }

        val supportButton = findViewById<FrameLayout>(R.id.write_to_support)
        supportButton.setOnClickListener {
viewModel.openSupport()
        }

    }
}