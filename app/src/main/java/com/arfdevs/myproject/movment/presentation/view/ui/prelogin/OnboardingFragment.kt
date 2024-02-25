package com.arfdevs.myproject.movment.presentation.view.ui.prelogin

import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.databinding.FragmentOnboardingBinding

class OnboardingFragment :
    BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {

    override fun initView() = with(binding)  {
        btnObGabung.text = "Let's watch!"
        btnObLewati.text = "Skip"
    }

    override fun initListener() {

    }
}