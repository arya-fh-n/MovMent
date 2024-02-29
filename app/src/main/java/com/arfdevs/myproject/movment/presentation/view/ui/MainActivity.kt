package com.arfdevs.myproject.movment.presentation.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.arfdevs.myproject.movment.databinding.ActivityMainBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.ENGLISH
import com.arfdevs.myproject.movment.presentation.helper.Constants.INDONESIAN
import com.arfdevs.myproject.movment.presentation.viewmodel.HomeViewModel
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
        viewModel.theme.observe(this) { isDarkTheme ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        viewModel.language.observe(this) { isIndonesian ->
            if (isIndonesian) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(INDONESIAN))
            } else {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(ENGLISH))
            }
        }
    }
}