package com.muhaammaad.challenge.util

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);

    var token: String
        get() = prefs.getString(TOKEN, "") ?: ""
        set(value) = prefs.edit().putString(TOKEN, value).apply()

    var email: String
        get() = prefs.getString(EMAIL, "") ?: ""
        set(value) = prefs.edit().putString(EMAIL, value).apply()

    companion object {
        const val TOKEN = "prefs_token"
        const val EMAIL = "prefs_email"
        const val PREFS_NAME = "com.muhaammaad.challenge.prefs"
        const val PRIVATE_MODE = 0
    }
}