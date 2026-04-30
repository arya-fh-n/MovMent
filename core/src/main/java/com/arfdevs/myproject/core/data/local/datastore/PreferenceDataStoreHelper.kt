package com.arfdevs.myproject.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.arfdevs.myproject.core.helper.Constants.LANGUAGE_KEY
import com.arfdevs.myproject.core.helper.Constants.ONBOARDING_KEY
import com.arfdevs.myproject.core.helper.Constants.THEME_KEY
import com.arfdevs.myproject.core.helper.Constants.USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface PreferenceDataStoreHelper {
    suspend fun putOnboardingState(value: Boolean)
    suspend fun getOnboardingState(): Boolean
    fun getOnboardingStateFlow(): Flow<Boolean>

    suspend fun putTheme(value: Boolean)
    suspend fun getTheme(): Boolean
    fun getThemeFlow(): Flow<Boolean>

    suspend fun putLanguage(value: String)
    suspend fun getLanguage(): String
    fun getLanguageFlow(): Flow<String>

    suspend fun putUID(value: String)
    suspend fun getUID(): String
    fun getUIDFlow(): Flow<String>

    suspend fun clearAllData()
}

class PreferenceDataStoreHelperImpl(private val dataStore: DataStore<Preferences>) : PreferenceDataStoreHelper {

    private object PreferencesKeys {
        val ONBOARDING_STATE = booleanPreferencesKey(ONBOARDING_KEY)
        val THEME_STATE = booleanPreferencesKey(THEME_KEY)
        val LANGUAGE_STATE = stringPreferencesKey(LANGUAGE_KEY)
        val UID_STATE = stringPreferencesKey(USER_ID)
    }

    override suspend fun putOnboardingState(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_STATE] = value
        }
    }

    override suspend fun getOnboardingState(): Boolean {
        return getOnboardingStateFlow().first()
    }

    override fun getOnboardingStateFlow(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_STATE] ?: false
        }
    }

    override suspend fun putTheme(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_STATE] = value
        }
    }

    override suspend fun getTheme(): Boolean {
        return getThemeFlow().first()
    }

    override fun getThemeFlow(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.THEME_STATE] ?: false
        }
    }

    override suspend fun putLanguage(value: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE_STATE] = value
        }
    }

    override suspend fun getLanguage(): String {
        return getLanguageFlow().first()
    }

    override fun getLanguageFlow(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.LANGUAGE_STATE] ?: ""
        }
    }

    override suspend fun putUID(value: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.UID_STATE] = value
        }
    }

    override suspend fun getUID(): String {
        return getUIDFlow().first()
    }

    override fun getUIDFlow(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.UID_STATE] ?: ""
        }
    }

    override suspend fun clearAllData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
