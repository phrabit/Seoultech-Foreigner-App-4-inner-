package com.example.a4_inner
import android.content.Context
import android.content.SharedPreferences

// object and functions for saving & retrieving shared preferences
object PreferenceHelper {

    private const val PREFERENCE_NAME = "OnBoarding Toggle"
    private const val BOOLEAN_KEY = "ONorOFF"

    private fun getPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun setBoolean(context: Context, value: Boolean) {
        val editor = getPreference(context).edit()
        editor.putBoolean(BOOLEAN_KEY, value)
        editor.apply()
    }

    fun getBoolean(context: Context): Boolean {
        return getPreference(context).getBoolean(BOOLEAN_KEY, true)
    }
}
