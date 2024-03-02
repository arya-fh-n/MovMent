package com.arfdevs.myproject.movment.presentation.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.os.bundleOf
import com.arfdevs.myproject.core.helper.launchAndCollectIn
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.ActivityMainBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.ENGLISH
import com.arfdevs.myproject.movment.presentation.helper.Constants.INDONESIAN
import com.arfdevs.myproject.movment.presentation.viewmodel.HomeViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getTheme()
        viewModel.getLanguage()
        initObserver()
    }

    private fun initObserver() {
        with(viewModel) {
            logEvent(FirebaseAnalytics.Event.APP_OPEN, bundleOf("App Opened" to "MainActivity"))
            theme.observe(this@MainActivity) { isDarkTheme ->
                AppCompatDelegate.setDefaultNightMode(
                    if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
            }

            language.observe(this@MainActivity) { isIndonesian ->
                if (isIndonesian) {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(INDONESIAN))
                } else {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(ENGLISH))
                }
            }
        }
    }
}