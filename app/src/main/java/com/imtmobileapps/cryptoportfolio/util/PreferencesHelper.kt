package com.imtmobileapps.cryptoportfolio.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class PreferencesHelper {

    companion object{
        private const val PREF_TIME = "Pref time"
        private const val PERSON_ID = "Person Id"
        private var prefs : SharedPreferences? = null
        @Volatile private var instance : PreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context):PreferencesHelper = instance ?: synchronized(
            LOCK){
            instance ?: buildHelper(context).also{
                instance = it
            }
        }

        private fun buildHelper(context:Context): PreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return PreferencesHelper()
        }

    }

    fun savePersonId(newpersonId: Int){
        prefs?.edit(commit = true){
            putInt(PERSON_ID, newpersonId)
        }

    }

    fun getCurrentPersonId() = prefs?.getInt(PERSON_ID, 0)

    fun saveUpdateTime(time: Long){
        prefs?.edit(commit = true){
            putLong(PREF_TIME, time)
        }
    }

    fun getUpdateTime() = prefs?.getLong(PREF_TIME, 0)

    // Set by user in SettingsFragment - preferences.xml
    fun getCacheDuration() = prefs?.getString("pref_cache_duration", "0")

}