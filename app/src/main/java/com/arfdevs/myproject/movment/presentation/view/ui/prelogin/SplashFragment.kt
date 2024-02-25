package com.arfdevs.myproject.movment.presentation.view.ui.prelogin

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentSplashBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.ALPHA_ANIMATION
import com.arfdevs.myproject.movment.presentation.helper.Constants.ROTATE_ANIMATION
import com.arfdevs.myproject.movment.presentation.helper.Constants.SCALE_X
import com.arfdevs.myproject.movment.presentation.helper.Constants.SCALE_Y
import com.arfdevs.myproject.movment.presentation.helper.Constants.TRANSLATION_X
import com.arfdevs.myproject.movment.presentation.helper.Constants.TRANSLATION_Y

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            animate(ivSplash, TRANSLATION_X, 0f, -20f)
        }


    }

    private fun animate(
        view: View,
        propertyName: String,
        startVal: Float,
        endVal: Float,
        duration: Long = 2000
    ) {
        view.run {
            when {
                propertyName.equals(TRANSLATION_X, true) -> {
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_X, startVal, endVal).apply {
                        this.duration = duration
                    }.start()
                }

                propertyName.equals(TRANSLATION_Y, true) -> {
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startVal, endVal).apply {
                        this.duration = duration
                    }.start()
                }

                propertyName.equals(SCALE_X, true) -> {
                    ObjectAnimator.ofFloat(view, View.SCALE_X, startVal, endVal).apply {
                        this.duration = duration
                    }.start()
                }

                propertyName.equals(SCALE_Y, true) -> {
                    ObjectAnimator.ofFloat(view, View.SCALE_Y, startVal, endVal).apply {
                        this.duration = duration
                    }.start()
                }

                propertyName.equals(ROTATE_ANIMATION, true) -> {
                    ObjectAnimator.ofFloat(view, View.ROTATION, startVal, endVal).apply {
                        this.duration = duration
                    }.start()
                }

                propertyName.equals(ALPHA_ANIMATION, true) -> {
                    ObjectAnimator.ofFloat(view, View.ALPHA, startVal, endVal).apply {
                        this.duration = duration
                    }.start()
                }

                else -> {
                    val marginParams = view.layoutParams as LinearLayout.LayoutParams
                    marginParams.setMargins(0, 0, 0, 60)
                }
            }
        }
    }

}