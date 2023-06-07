package com.bangkit.emergenz.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.bangkit.emergenz.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var dataStore: DataStore<Preferences>

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            dataStore = requireContext().dataStore

            val darkModePreference = findPreference<ListPreference>("theme")
            lifecycleScope.launch {
                val currentDarkModeValue = getCurrentDarkModeValue()
                darkModePreference?.value = currentDarkModeValue
            }

            darkModePreference?.setOnPreferenceChangeListener { preference, newValue ->
                val selectedValue = newValue as String
                updateDarkMode(selectedValue)
                true
            }
        }

        private suspend fun getCurrentDarkModeValue(): String {
            val darkModeKey = stringPreferencesKey("dark_mode")
            val darkModeValueFlow: Flow<String> = dataStore.data.map { preferences ->
                preferences[darkModeKey] ?: "otomatis"
            }
            return darkModeValueFlow.first()
        }

        private fun updateDarkMode(selectedValue: String) {
            lifecycleScope.launch {
                val darkModeKey = stringPreferencesKey("dark_mode")
                dataStore.edit { preferences ->
                    preferences[darkModeKey] = selectedValue
                }
                applyTheme(selectedValue)
            }
        }

        private fun applyTheme(selectedValue: String){
            val nightMode = when (selectedValue) {
                "terang" -> AppCompatDelegate.MODE_NIGHT_NO
                "gelap" -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }
}