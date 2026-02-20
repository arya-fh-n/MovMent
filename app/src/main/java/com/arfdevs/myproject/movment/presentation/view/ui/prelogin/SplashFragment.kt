package com.arfdevs.myproject.movment.presentation.view.ui.prelogin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.helper.SplashState
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentSplashBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.SPLASH_DELAY
import com.arfdevs.myproject.movment.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()

    override fun initView() {
        with(binding) {
            ivSplash.load(R.drawable.splash_icon)
            tvSplashTitle.text = getString(R.string.app_name)
            splashAnim()
            viewModel.getOnboardingState()
        }
    }

    override fun initListener() {}

    override fun initObserver() {
        with(viewModel) {
            onboardingState.observe(viewLifecycleOwner) { state ->
                Handler(Looper.getMainLooper()).postDelayed({
                    val navController =
                        activity?.supportFragmentManager?.findFragmentById(R.id.main_navigation_container)
                            ?.findNavController()
                    when (state) {
                        is SplashState.Main -> {
                            navController?.navigate(R.id.action_splashFragment_to_dashboardFragment)
                        }

                        is SplashState.Profile -> {
                            navController?.navigate(R.id.action_splashFragment_to_profileFragment)
                        }

                        is SplashState.Onboarding -> {
                            navController?.navigate(R.id.action_splashFragment_to_onboardingFragment)
                        }

                        else -> {
                            navController?.navigate(R.id.action_splashFragment_to_loginFragment)
                        }
                    }
                }, SPLASH_DELAY)
            }
        }
    }

    private fun splashAnim() = with(binding) {
        val logoFade = ObjectAnimator.ofFloat(ivSplash, View.ALPHA, 0f, 1f).apply {
            this.duration = 700
        }

        val logoYSlide = ObjectAnimator.ofFloat(ivSplash, View.TRANSLATION_Y, -75f, 25f).apply {
            this.duration = 1200
        }

        val titleFade = ObjectAnimator.ofFloat(tvSplashTitle, View.ALPHA, 0f, 10f).apply {
            tvSplashTitle.isVisible = true
            this.duration = 1000
        }

        val titleYSLide =
            ObjectAnimator.ofFloat(tvSplashTitle, View.TRANSLATION_Y, 50f, -250f).apply {
                this.duration = 1400
            }

        val titleAnimator = AnimatorSet().apply {
            play(titleFade).with(titleYSLide)
        }

        AnimatorSet().apply {
            play(logoYSlide).with(logoFade).before(titleAnimator)
            start()
        }

    }

}
