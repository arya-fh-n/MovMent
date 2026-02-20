package com.arfdevs.myproject.movment.presentation.view.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.CustomSnackbarBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout

object CustomSnackbar {

    @SuppressLint("RestrictedApi")
    fun show(
        context: Context,
        root: View,
        title: String = "",
        message: String,
        action: () -> Unit? = { }
    ) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = CustomSnackbarBinding.inflate(inflater)
        val snackBar = Snackbar.make(root, "", Snackbar.LENGTH_INDEFINITE)

        val snackbarLayout = snackBar.view as SnackbarLayout

        if (title.isNotEmpty()) {
            binding.tvSnackbarTitle.text = title
        } else {
            binding.tvSnackbarTitle.isGone = true
        }

        binding.tvSnackbarMessage.text = message

        snackbarLayout.apply {
            val layoutParams = layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.gravity = Gravity.TOP
            this.layoutParams = layoutParams
        }

        snackbarLayout.addView(binding.root, 0)

        snackBar.apply {
            view.setBackgroundColor(Color.TRANSPARENT)
            animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE

            val slideDownAnim = AnimationUtils.loadAnimation(context, R.anim.slide_down)
            val slideUpAnim = AnimationUtils.loadAnimation(context, R.anim.slide_up)

            view.startAnimation(slideDownAnim)

            slideDownAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    view.startAnimation(slideUpAnim)
                }

                override fun onAnimationRepeat(p0: Animation?) {}

            })

            slideUpAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    view.isVisible = false
                    dismiss()
                    action.invoke()
                }

                override fun onAnimationRepeat(p0: Animation?) {}

            })
            show()
        }

    }
}
