package com.example.myplaylistmaker.utility

object StringUtils {
    fun getTrackCountString(count: Int): String {
        val lastDigit = count % 10
        val lastTwoDigits = count % 100

        return when {
            lastTwoDigits in 11..19 -> "$count треков"
            lastDigit == 1 -> "$count трек"
            lastDigit in 2..4 -> "$count трека"
            else -> "$count треков"
        }
    }
    fun getMinutesCountString(count: Int): String {
        val lastDigit = count % 10
        val lastTwoDigits = count % 100

        return when {
            lastTwoDigits in 11..19 -> "$count минут"
            lastDigit == 1 -> "$count минута"
            lastDigit in 2..4 -> "$count минуты"
            else -> "$count минут"
        }
    }
}