package com.arfdevs.myproject.movment.presentation.view.ui.prelogin

import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentOnboardingBinding
import com.arfdevs.myproject.movment.presentation.view.adapter.OnboardingPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingFragment :
    BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {

    private val onboardingImages = listOf(
        R.drawable.movment_1,
        R.drawable.movment_2,
        R.drawable.movment_3
    )

    private val onboardingTitles = listOf(
        "Your favorite movies, in one app.",
        "All great selections, from all genres",
        "Join our MovMent!"
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

        btnObJoin.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_registerFragment)
        }

        btnObSkip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.vpOnboarding.unregisterOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
        })
    }
}