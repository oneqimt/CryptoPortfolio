package com.imtmobileapps.cryptoportfolio.view


import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.View
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.imtmobileapps.cryptoportfolio.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val numberPreference : EditTextPreference? = findPreference("pref_cache_duration")

        numberPreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER

        }

        numberPreference?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    "Not set"
                } else {
                    "Current value: ${text} seconds"

                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(Build.VERSION.SDK_INT >= 23){
            view.setBackgroundColor(resources.getColor(R.color.light_grey, null))
        }

    }
}
