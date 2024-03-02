package com.arfdevs.myproject.movment.presentation.view.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.navigation.fragment.findNavController
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentSettingsBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.ENGLISH
import com.arfdevs.myproject.movment.presentation.helper.Constants.INDONESIAN
import com.arfdevs.myproject.movment.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()

    override fun initView() = with(binding) {
        toolbarSettings.title = getString(R.string.app_name)
        tvSettingsTitle.setText(R.string.tv_settings_title)

        tvEn.setText(R.string.loc_english)
        tvId.setText(R.string.loc_indonesian)

        tvLight.setText(R.string.theme_light)
        tvDark.setText(R.string.theme_dark)

        viewModel.getLanguage()
        viewModel.getTheme()
    }

    override fun initListener() = with(binding) {
        toolbarSettings.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            val selectedLanguage: String = when (isChecked) {
                true -> {
                    updateLanguage(INDONESIAN)
                    INDONESIAN
                }

                false -> {
                    updateLanguage(ENGLISH)
                    ENGLISH
                }
            }
            viewModel.saveLanguage(selectedLanguage)
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    updateTheme(true)
                    viewModel.saveTheme(true)
                }

                false -> {
                    updateTheme(false)
                    viewModel.saveTheme(false)
                }
            }
        }
    }

    override fun initObserver() {
        with(viewModel) {
            theme.observe(viewLifecycleOwner) {
                binding.switchTheme.isChecked = it
            }
            language.observe(viewLifecycleOwner) {
                binding.switchLanguage.isChecked = it
            }
        }
    }

    private fun updateTheme(isDarkTheme: Boolean) {
        val theme = if (isDarkTheme) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(theme)
    }

    private fun updateLanguage(language: String) {
        val locale = if (language.equals(ENGLISH, true)) {
            LocaleListCompat.forLanguageTags(ENGLISH)
        } else {
            LocaleListCompat.forLanguageTags(INDONESIAN)
        }

        AppCompatDelegate.setApplicationLocales(locale)
    }

}