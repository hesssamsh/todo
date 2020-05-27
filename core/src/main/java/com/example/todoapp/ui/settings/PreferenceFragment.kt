package com.example.todoapp.ui.settings

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.todoapp.R
import com.example.todoapp.ui.MainViewModel

class PreferenceFragment : PreferenceFragmentCompat() {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val nightModePrefs =
            findPreference<SwitchPreference>(getString(R.string.night_mode_prefs_key))

        nightModePrefs?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            viewModel.setNightMode(newValue as Boolean)
            true
        }

        val versionNamePrefsKey =
            findPreference<Preference>(getString(R.string.version_name_prefs_key))

        viewModel.versionName.observe(requireActivity(), Observer {
            versionNamePrefsKey?.title = getString(R.string.version, it)
        })
    }
}
