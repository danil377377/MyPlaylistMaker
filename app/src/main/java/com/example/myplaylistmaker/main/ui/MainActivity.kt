package com.example.myplaylistmaker.main.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.R.id.media
import com.example.myplaylistmaker.R.id.settings
import com.example.myplaylistmaker.media.ui.MediaActivity
import com.example.myplaylistmaker.search.ui.SearchActivity
import com.example.myplaylistmaker.settings.ui.SettingActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search)
        val mediaButton = findViewById<Button>(media)
        val settingsButton = findViewById<Button>(settings)

        searchButton.setOnClickListener{
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }



        mediaButton.setOnClickListener {
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }


        settingsButton.setOnClickListener {
            val displayIntent = Intent(this, SettingActivity::class.java)
            startActivity(displayIntent)
        }
    }

}


