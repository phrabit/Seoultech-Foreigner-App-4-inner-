package com.example.a4_inner
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

// object and functions for saving & retrieving shared preferences
object PreferenceHelper {

    private const val PREFERENCE_NAME = "OnBoarding Toggle"
    private const val BOOLEAN_KEY = "ONorOFF"
    private const val RECENT_BUILDING_1 = "Recent building 1"
    private const val RECENT_BUILDING_2 = "Recent building 2"
    private const val RECENT_BUILDING_3 = "Recent building 3"

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

    fun getRecentDestinations(context: Context): Array<String?> {
        val recent_buildings = arrayOf(
            getPreference(context).getString(RECENT_BUILDING_1, null),
            getPreference(context).getString(RECENT_BUILDING_2, null),
            getPreference(context).getString(RECENT_BUILDING_3, null)
        )
        return recent_buildings
    }

    fun setRecentDestinations(context: Context, recent_building:String) {
        val building1 = getPreference(context).getString(RECENT_BUILDING_1, null)
        val building2 = getPreference(context).getString(RECENT_BUILDING_2, null)
        getPreference(context).edit().putString(RECENT_BUILDING_1, recent_building).apply()
        getPreference(context).edit().putString(RECENT_BUILDING_2, building1).apply()
        getPreference(context).edit().putString(RECENT_BUILDING_3, building2).apply()
    }
}
