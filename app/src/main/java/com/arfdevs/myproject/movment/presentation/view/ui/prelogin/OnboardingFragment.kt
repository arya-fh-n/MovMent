package com.arfdevs.myproject.movment.presentation.view.ui.prelogin

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentOnboardingBinding
import com.arfdevs.myproject.movment.presentation.view.adapter.OnboardingPagerAdapter
import com.arfdevs.myproject.movment.presentation.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingFragment :
    BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()

    private val onboardingImages = listOf(
        R.drawable.movment_1,
        R.drawable.movment_2,
        R.drawable.movment_3
    )

    private val onboardingTitles = listOf(
        R.string.ob_title_1,
        R.string.ob_title_2,
        R.string.ob_title_3
    )

    override fun initView() = with(binding) {
        btnObJoin.text = getString(R.string.btn_join)
        btnObSkip.text = getString(R.string.btn_skip)
    }

    override fun initListener() = with(binding) {
        val vp = vpOnboarding
        val tabLayout = tabs
        val adapter = OnboardingPagerAdapter(onboardingImages, onboardingTitles)

        vp.adapter = adapter

        TabLayoutMediator(tabLayout, vp) { _, _ -> }.attach()

        viewModel.run {
            saveOnboardingState(true)
            logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundleOf("Onboarding shown" to "OnboardingFragment"))
        }

        btnObJoin.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_registerFragment)
        }

        btnObSkip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
        }
    }

    override fun initObserver() {

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.vpOnboarding.unregisterOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
        })
    }
}
