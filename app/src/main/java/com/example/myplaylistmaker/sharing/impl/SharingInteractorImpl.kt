package com.example.myplaylistmaker.sharing.impl

import android.content.Context
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.sharing.domen.ExternalNavigator
import com.example.myplaylistmaker.sharing.domen.SharingInteractor
import com.example.myplaylistmaker.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    val context: Context
) : SharingInteractor {
    override fun shareApp() {
        context.startActivity(externalNavigator.shareLink(getShareAppLink()))
    }

    override fun openTerms() {
        context.startActivity(externalNavigator.openLink(getTermsLink()))

    }

    override fun openSupport() {
        context.startActivity(externalNavigator.openEmail(getSupportEmailData()))
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.share_link)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(context.getString(R.string.subject_message), context.getString(R.string.message), context.getString(R.string.my_email))
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.contract_link)
    }
}