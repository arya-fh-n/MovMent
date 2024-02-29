package com.arfdevs.myproject.core.data.local.sharedpref

import android.content.SharedPreferences
import com.arfdevs.myproject.core.helper.Constants.LANGUAGE_KEY
import com.arfdevs.myproject.core.helper.Constants.ONBOARDING_KEY
import com.arfdevs.myproject.core.helper.Constants.THEME_KEY
import com.arfdevs.myproject.core.helper.Constants.USER_ID

interface SharedPreferencesHelper {

    fun putOnboardingState(value: Boolean)

    fun getOnboardingState(): Boolean

    fun putTheme(value: Boolean)

    fun getTheme(): Boolean

    fun putLanguage(value: String)

    fun getLanguage(): String

    fun putUID(value: String)

    fun getUID(): String

    fun removeValue(key: String)

    fun clearAllData()

}

class SharedPreferencesHelperImpl(private val sharedPreferences: SharedPreferences): SharedPreferencesHelper {

    override fun putOnboardingState(value: Boolean) {
        sharedPreferences.edit().putBoolean(ONBOARDING_KEY, value).apply()
    }

    override fun getOnboardingState(): Boolean {
        return sharedPreferences.getBoolean(ONBOARDING_KEY, false)
    }

    override fun putTheme(value: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_KEY, value).apply()
    }

    override fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }

    override fun putLanguage(value: String) {
        sharedPreferences.edit().putString(LANGUAGE_KEY, value).apply()
    }

    override fun getLanguage(): String {
        return sharedPreferences.getString(LANGUAGE_KEY, "").toString()
    }

    override fun putUID(value: String) {
        sharedPreferences.edit().putString(USER_ID, value).apply()
    }

    override fun getUID(): String {
        return sharedPreferences.getString(USER_ID, "").toString()
    }

    override fun removeValue(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }

}