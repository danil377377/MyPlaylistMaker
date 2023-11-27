package com.example.myplaylistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.myplaylistmaker.R.id.media
import com.example.myplaylistmaker.R.id.settings

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search)

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Поиск, поиск, поисковичёк", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        searchButton.setOnClickListener(imageClickListener)


        val mediaButton = findViewById<Button>(media)
        mediaButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "медиа, медь, медяшка", Toast.LENGTH_SHORT).show()
        }

        val settingsButton = findViewById<Button>(settings)
        settingsButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "настройки, настойки, настоятели", Toast.LENGTH_SHORT).show()
        }
    }
}


