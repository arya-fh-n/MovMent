package com.arfdevs.myproject.movment.presentation.view.ui.auth

import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.helper.enabled
import com.arfdevs.myproject.core.helper.launchAndCollectIn
import com.arfdevs.myproject.core.helper.onError
import com.arfdevs.myproject.core.helper.onLoading
import com.arfdevs.myproject.core.helper.onSuccess
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentProfileBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.USERNAME_MIN_LENGTH
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.AuthViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: AuthViewModel by viewModel()

    override fun initView() = with(binding) {
        btnProfileFinish.enabled(false)

        tvProfileTitle.text = getString(R.string.tv_profile_title)
        tiUsername.hint = getString(R.string.ti_username_hint)

        etUsername.doAfterTextChanged {
            validateName()
        }

        btnProfileFinish.text = getString(R.string.btn_profile_finish)
        tvProfileTNC.text = getString(R.string.tv_tnc)
    }

    override fun initListener() = with(binding) {
        btnProfileFinish.setOnClickListener {
            val username = etUsername.text.toString().trim()

            collectProfile(username)
        }
    }

    override fun initObserver() {}

    private fun collectProfile(username: String) = with(binding) {
        viewModel.updateUsername(username).launchAndCollectIn(viewLifecycleOwner) { state ->
            state.onSuccess { success ->
                loadingOverlay.visible(false)
                loadingAnim.visible(false)

                if (success) {
                    viewModel.logEvent(
                        FirebaseAnalytics.Event.SCREEN_VIEW,
                        bundleOf("New User Profile Name" to username)
                    )
                    context?.let {
                        CustomSnackbar.show(
                            it,
                            root,
                            "Register complete!",
                            "Welcome to MovMent!"
                        ) {
                            findNavController().navigate(R.id.action_profileFragment_to_dashboardFragment)
                        }
                    }
                }
            }.onError { e ->
                loadingOverlay.visible(false)
                loadingAnim.visible(false)

                context?.let {
                    CustomSnackbar.show(
                        it,
                        root,
                        getString(R.string.err_title_register_failed),
                        e.localizedMessage ?: e.message.toString()
                    )
                }

            }.onLoading {
                loadingOverlay.visible(true)
                loadingAnim.visible(true)
            }
        }
    }

    private fun validateName() = with(binding) {
        val username = etUsername.text.toString()

        val isBlank = username.isBlank()
        val isShort = username.length < USERNAME_MIN_LENGTH

        when {
            isBlank -> {
                tiUsername.apply {
                    error = context.getString(R.string.err_username_empty)
                    isErrorEnabled = true
                }
                btnProfileFinish.enabled(false)
            }

            isShort -> {
                tiUsername.apply {
                    error = context.getString(R.string.err_username_short)
                    isErrorEnabled = true
                }
                btnProfileFinish.enabled(false)
            }

            else -> {
                tiUsername.apply {
                    isErrorEnabled = false
                }
                btnProfileFinish.enabled(true)
            }

        }
    }

}
