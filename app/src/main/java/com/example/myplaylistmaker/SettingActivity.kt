package com.example.myplaylistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout

class SettingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val ContractButton = findViewById<FrameLayout>(R.id.contract)
        ContractButton.setOnClickListener{
            val displayIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(displayIntent)
        }


        val supportButton = findViewById<FrameLayout>(R.id.write_to_support)
        supportButton.setOnClickListener {
            val subjectMessage  = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("danil377377@gmail.com"))
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subjectMessage)
            startActivity(shareIntent)
        }
        val shareButton = findViewById<FrameLayout>(R.id.share_button)
        shareButton.setOnClickListener {

            val message = "https://practicum.yandex.ru/android-developer/"
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(shareIntent)
        }
    }
}