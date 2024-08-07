package com.example.myplaylistmaker.sharing.domen

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.sharing.model.EmailData

class ExternalNavigator {
    fun shareLink(link:String): Intent{

        val message = link
       return Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
           addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
    fun openLink(link:String):Intent{
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    }
    fun openEmail(emailData:EmailData): Intent{
        val subjectMessage  = emailData.subjectMessage
        val message = emailData.message
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = Uri.parse("mailto:")
        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.myEmail))
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subjectMessage)
        return shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    }
}