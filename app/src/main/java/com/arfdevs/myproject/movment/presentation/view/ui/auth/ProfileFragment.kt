package com.arfdevs.myproject.movment.presentation.view.ui.auth

import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.helper.UiState
import com.arfdevs.myproject.core.helper.enabled
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentProfileBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.USERNAME_MIN_LENGTH
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.AuthViewModel
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
            viewModel.updateUsername(username)
        }
    }

    override fun initObserver() = with(viewModel) {
        updateUsernameState.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiState.Loading -> handleLoading(state is UiState.Loading)
                UiState.Empty -> handleError(getString(R.string.err_empty))
                is UiState.Error -> handleError(state.message)
                UiState.ErrorConnection -> handleError(getString(R.string.err_connection))
                is UiState.Success -> {
                    if (state.data) handleSuccess()
                    else handleError(getString(R.string.err_failed))
                }
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) = with(binding) {
        loadingOverlay.visible(isLoading)
        loadingAnim.visible(isLoading)
    }

    private fun handleError(message: String) = with(binding) {
        context?.let {
            CustomSnackbar.show(
                it, root,
                getString(R.string.err_title_register_failed),
                message
            )
        }
    }

    private fun handleSuccess() = with(binding) {
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
